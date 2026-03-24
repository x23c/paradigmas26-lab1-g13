import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[Models.Subscription] = FileIO.readSubscriptions()

    val allPosts: List[(String, String)] = subscriptions.map { case (name, url) =>
      println(s"Fetching posts from: $url")
      val stringFeed = FileIO.downloadFeed(url)
      val posts = Parser.parseRedditFeed(stringFeed)
      (url, posts.map(Formatters.formatPost).mkString("\n\n"))
    }

    val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")

    println(output)
  }
}
