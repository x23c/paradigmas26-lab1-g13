import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[Models.Subscription] = FileIO.readSubscriptions()

    val allPosts: List[(String, String)] = subscriptions.map { case (name, url) =>
      println(s"Fetching posts from: $url")
      val stringFeed = FileIO.downloadFeed(url)
      val posts = Parser.parseRedditFeed(stringFeed).filter { case (_, title, selftext, _) =>
        title.nonEmpty && selftext.nonEmpty && selftext.trim.nonEmpty
      }
      (url, posts.map(Formatters.formatPost).mkString("\n\n"))
      //Aca filtre el resultado del parser con la funcion de alto orden filter 
      //y los 3 parametros q puse son en orden para cuando no tiene titulo, no tiene texto y cuando el texto es vacio
    }

    val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")

    println(output)
  }
}
