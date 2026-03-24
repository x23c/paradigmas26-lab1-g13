import scala.io.Source
import Models._
import org.json4s._  
import org.json4s.jackson.JsonMethods._  // this lets us use parse() to turn a JSON string into a usable object

object FileIO {
  implicit val formats: Formats = DefaultFormats  // needed by json4s to convert JSON values into Scala types like String (extract[String])
  
  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): List[Subscription] = {  // returns a list of type Subscriptions

    // opens the file at the given path and creates a Source object to read its contents
    val source = Source.fromFile("subscriptions.json")
    
    try {
      val content = source.mkString  // reads the entire file and concatenates it into a single String

      val json = parse(content)  // converts the string into a JSON object we can work with

      val elements = json.children  // converts the JSON array into a List[JValue], where each element represents one subscription object

      // Go through each item and turn it into a Subscription tuple
      elements.map { elem =>  // uses 'map' to transform each JValue element into a Subscription tuple
        val name = (elem \ "name").extract[String]
        val url  = (elem \ "url").extract[String]
        (name, url)  // returns a tuple (Subscription) for this element
      }
    } finally {
      source.close()  // ensures the file is closed even if an error occurs, preventing resource leaks
    }
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
    
  
