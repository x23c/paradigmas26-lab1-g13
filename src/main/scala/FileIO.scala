import scala.io.Source
import Models._
import org.json4s._
import org.json4s.jackson.JsonMethods._

object FileIO {
  implicit val formats: Formats = DefaultFormats //json4 lo necesita para convertir de JSON a SCALA
  // Pure function to read subscriptions from a JSON file
  //funcion para desmenuzar las cosas JSON
  def readSubscriptions(): Option[List[Subscription]] = {

    val toParse = Source.fromFile("subscriptions.json") //abro el archivo y lo leo

    try{
      val contenido = toParse.mkString //guardo las giladas del archivo aca en string

      val parseado = parse(contenido) //pasa de string a JSON


    //.children: dame todos los elementos q estan en el array de esste JSON
      val elementos = parseado.children  //de JSON a List[JValue] asi puedo manipularlo
    //EJEMPLO: si json = [obj1,obj2,obj3] entonces json.children = List(obj1,obj2,obj3)
    

      //ahora itero en esta lista de jvalues y lo convierto al tipo subscription
      //para cada elemento de la lista elementos, transformalo en una tupla (subredditname,url)
      elementos.map{elem =>
      val name = (elem \ "name").extract[String] //consigo el elemento name en el JSON y lo convierto a string
      val url = (elem \ "url").extract[String]
      (name,url) //esto es lo que devuelve 
      
      }

    } finally {
      toParse.close()
    }


  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): Option[String] = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
