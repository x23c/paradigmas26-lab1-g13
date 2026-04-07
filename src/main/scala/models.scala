

object Models {

  type Subscription = (String, String) // (subredditName, url) 
  type Post = (String, String, String, String, Int, String) // (subreddit, title, selftext, formattedDate, score, url)
  type PalabraFrequencia = (String, Int) // Tipo para representar frecuencias: (palabra, cantidad)

  def createPost(subreddit: String, title: String, selftext: String, formattedDate: String, score: Int, url: String): Post = {
      (subreddit, title, selftext, formattedDate, score, url)
  }
  /*
  ,
  {
    "name": "Learn Python",
    "url": "https://www.reddit.com/r/learnpython/.json?count=10"
  }
  */
}
