object Models {

  type Subscription = (String, String) // (subredditName, url) 
  type Post = (String, String, String, String) // (subreddit, title, selftext, formattedDate)
  type PalabraFrequencia = (String, Int) // Tipo para representar frecuencias: (palabra, cantidad)

  def createPost(subreddit: String, title: String, selftext: String, formattedDate: String): Post = {
      (subreddit, title, selftext, formattedDate)
  }
  /*
  ,
  {
    "name": "Learn Python",
    "url": "https://www.reddit.com/r/learnpython/.json?count=10"
  }
  */
}
