import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._  // this lets us use parse() to turn a JSON string into a usable object
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object FileIO {
  // needed by json4s to convert JSON values into Scala types like String (extract[String])
  implicit val formats: Formats = DefaultFormats  

  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): Option[List[Main.Subscription]] = {
    try {
      val source = Source.fromFile("subscriptions.json")
        try {
          val content = source.mkString
          val json = parse(content).children 
          //parse devuelve un Jvalue que representa todo el array json
          //chiñdren devuelve una lista de elems del array json 
          // (objeto json 1, objeto json 2, objeto json 3...)

          val subs = json.map {elem =>
            val name = (elem \ "name").extract[String] 
            val url = (elem \ "url").extract[String]
            (name, url)       // los transformo a tipo Subscription 
          }
          Some(subs)  // Option[List[Subscription]]
        }
        finally {
          source.close()
        }
      }
      catch {
        case _ : Exception => None 
      } 
  }




    /* jsonfeed (String)
    ↓ parse
    JValue
    ↓ data
    ↓ children
    List[JValue]
    ↓ map
    Post */

  //en main se llama a downloadfeed con el url de readsubscription (subscription._2). se guarda el json feed en una variable que se llama jsonfeed. por eso falta parsear, hacer child y map de jsonfeed
  def extractPosts (subreddit : String, jsonfeed : String): Option[List[Main.Post]] = {
    try {
      val json = (parse(jsonfeed) \ "data" \ "children").children  // parse devuelve un jvalue, accedo a data, children 
      // .children devuelve una lista de elems del array json [post 1, post 2, post 3...] que son Jvalues

      // elem representa un unico post por vez
      val posts = json.map { elem => 
        val title = (elem \ "data" \ "title").extractOpt[String].getOrElse("")
        val selftext = (elem \ "data" \ "selftext").extractOpt[String].getOrElse("")
        val createdUtc = (elem \ "data" \ "created_utc").extractOpt[Long].getOrElse(0L)

        // Formato de la fecha a una forma legible
        val formattedDate = Instant.ofEpochSecond(createdUtc)
          .atZone(ZoneId.of("UTC"))
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        (subreddit, title, selftext, formattedDate)
        } // retorna una List[Post]

        Some(posts)  // retorna un Option[List[Post]]
    }
    catch {
      case _: Exception => None
    }

  }


  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): Option[String] = {
    try {
      // Source.fromURL puede lanzar excepciones (403, timeout, URL inválida, etc.)
      // por eso se encapsulan y se representan como Option   
      val source = Source.fromURL(url)

      try {
        // si la descarga es exitosa, se devuelve el contenido dentro de Some
        // la función devuelve Option[String]:
        // Some(contenido) si la descarga es exitosa
        // None si ocurre una excepción
        Some(source.mkString)  
      }
      finally {
        // el recurso se cierra siempre, incluso si ocurre una excepción durante la lectura
        source.close()  // source solo existe dentro del scope del try mas grande, como este finally esta adentro, se cierra de forma segura
      }
    }
    catch {
      // ante cualquier error, se devuelve None en lugar de propagar una excepción
      case _: Exception => None
    }
  }
}