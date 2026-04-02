import scala.io.Source
import Models._
import org.json4s._  
import scala.util.Try
import org.json4s.jackson.JsonMethods._  // this lets us use parse() to turn a JSON string into a usable object

object FileIO {
  implicit val formats: Formats = DefaultFormats  // needed by json4s to convert JSON values into Scala types like String (extract[String])
  
  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): Option[List[Subscription]] = {  // returns a list of type Subscriptions

    // opens the file at the given path and creates a Source object to read its contents
    
    Try {
      val source = Source.fromFile("subscriptions.json")
      val content = source.mkString  // reads the entire file and concatenates it into a single String
      source.close()

      val json = parse(content)  // converts the string into a JSON object we can work with

      val elements = json.children  // converts the JSON array into a List[JValue], where each element represents one subscription object

      // Go through each item and turn it into a Subscription tuple
      elements.map { elem =>  // uses 'map' to transform each JValue element into a Subscription tuple
        val name = (elem \ "name").extract[String]
        val url  = (elem \ "url").extract[String]
        (name, url)  // returns a tuple (Subscription) for this element
      }
    }.toOption
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): Option[String] = {
    Try{
      val source = Source.fromURL(url)

      val content = source.mkString
      source.close()
      content

    }.toOption
  }
}
    
  
