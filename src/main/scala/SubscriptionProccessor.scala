

object SubscriptionProcessor {

    // Procesa todas las suscripciones
  def processAllSubscriptions(subscriptions: List[Models.Subscription]): List[(String, String)] = {
    subscriptions.map { case (name, url) =>
      processSingleSubscription(name, url)
    }
  }

  // Procesa una suscripción individual
  def processSingleSubscription(name: String, url: String): (String, String) = {
    println(s"Fetching posts from: $url")
    
    val posts = Parser.parseAndFilterPosts(url)
    
    val totalScore = PostAnalyzer.calculateTotalScore(posts)
    val top5posts = Formatters.formatTop5Posts(posts)
    val top5words = WordsAnalyzer.analyzeTopWords(posts)
    
    val finalText = Formatters.buildSubscriptionText(name, totalScore, top5words, top5posts)
    
    (name, finalText)
  }
}