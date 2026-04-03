import Models._

object Formatters {

  // Pure function to format posts from a subscription
  def formatSubscription(url: String, posts: String): String = {
    val header = s"\n${"=" * 80}\nPosts from: $url \n${"=" * 80}"
    val formattedPosts = posts.take(1500)
    header + "\n" + formattedPosts
  }

  // Formato de cada post
  def formatPost(post: Post): String = {
    val (subreddit, title, selftext, date, score, url) = post
    
    s"""
       |${"-" * 80}
       |Subreddit: $subreddit
       |Titulo: $title
       |Contenido: ${selftext.take(150)}
       |Fecha: $date
       |Puntuación: $score
       |Url: $url
       |${"-" * 80}""".stripMargin
  }
}
