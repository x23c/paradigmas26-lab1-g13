import Models._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter
import scala.util.Try

object Parser {

    // Como convertir JSON a objetos de Scala
    implicit val formats: Formats = DefaultFormats

    def parseRedditFeed(stringFeed: String): List[Post] = {
        
        // Ejemplo del Feed ya parseado:
        // '{"data": {"children": [{"data": {"subreddit": "scala", "title": "Hola", "selftext": "texto", "created_utc": 123}]}}'

        // Función auxiliar para extraer un post de la List[JValue]
        // child es UN objeto JSON dentro de List[JValue]
        // "\" es el operador de navegacion de JSON
        // dentro del campo data, extraigo los campos subreddit, title, selftext y created_utc
        // devuelve un Post con esos campos
        // extractOpt devuelve una Option, getOrElse devuelve el valor o un valor por defecto si no existe
        def extractPost(child: JValue): Post = {
            val data = child \ "data"
    
            val subreddit = (data \ "subreddit").extractOpt[String].getOrElse("")
            val title = (data \ "title").extractOpt[String].getOrElse("")
            val selftext = (data \ "selftext").extractOpt[String].getOrElse("")
            val createdUtc = (data \ "created_utc").extractOpt[Long].getOrElse(0L)

            // Formato de la fecha a una forma legible
            val formattedDate = Instant.ofEpochSecond(createdUtc)
                .atZone(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            Models.createPost(subreddit, title, selftext, formattedDate)
        }
  
        // Función auxiliar para obtener el array children
        // children es un array JSON con todos los posts del feed
        // me devuelve una list[JValue], cada uno representando un post
        // si no existe el campo children, devuelve una lista vacia
        def getChildren(jsonFeed: JValue): List[JValue] = 
            (jsonFeed \ "data" \ "children")
                .extractOpt[List[JValue]]
                .getOrElse(Nil)
        
  
        // Lógica principal
        // se parsea el stringFeed y se usa map para aplicar las funciones auxiliares
        // en caso de error devuelve una lista vacia y un mensaje de error (se maneja con option)
        // en caso de exito, devuelve una lista de Post
        Try(parse(stringFeed))
        .toOption
        .map { jsonFeed =>
            val children = getChildren(jsonFeed)
            children.map(extractPost)
        }
        .getOrElse {
            println("Error: No se pudo parsear el string Feed.")
            List.empty[Post]
        }
    }
}   