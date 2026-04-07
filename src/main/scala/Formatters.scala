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

  // Formatea los top 5 posts para mostrar
  def formatTop5Posts(posts: List[(String, String, String, String, Int, String)]): String = {
    posts.take(5).map { post => 
      val (_, title, _, date, _, url) = post
      s"Titulo: $title\nFecha: $date\nURL: $url"
    }.mkString("\n\n")
  }

  // Construye el texto final de la suscripción
  def buildSubscriptionText(name: String, totalScore: Int, top5words: String, top5posts: String): String = {
    s"""
      |Subscription: $name 
      |Total score: $totalScore
      |Top 5 words: $top5words
      |
      |Top 5 posts: 
      |$top5posts
      |
      |""".stripMargin
  }
}
