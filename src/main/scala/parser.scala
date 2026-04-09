import Models._
import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._

object Parser {

  implicit val formats: Formats = DefaultFormats

    def parseRedditFeed(stringFeed:String): List[Post]={

        def extractPost(child:Jvalue): Option[Post] ={
            val data = child \ "data"

            for {
                subreddit <- (data\"subreddit").extractOpt[String]
                title <- (data \"title").extractOpt[String]
                selftext <- (data \ "selftext").extractOpt[String]
                createdUtc <- (data \ "created_utc")extractOpt[Long]
            } yield{
            val formattedDate = Instant.ofEpochSecond(createdUtc)
                .at(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern("yyy-MMM-ddd HH:mm:ss"))

            Models.createPost(subreddit,title,selftext,formattedDate)
            }
        }

        def getChildren(jsonFeed: JValue): List[JValue] = (
            jsonfFeed \ "data" \ "children"
        ).extractOpt[List[JValue]]
        .getOrElse(Nil)

        //logica principal del parse reddit feed
        Try(parse(stringFeed))
        .toOption
        .map { jsonFeed =>
            val children = getChildren(jsonFeed)
            children.flatMap(extractPost)
        }

        }

    
}