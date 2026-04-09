package models
object Models {
  type Subscription = (String, String) // (subredditName, URL)

  type Post =(String, String, String, String) //subrredit, titulo, selftext, _

  def createPost(subreddit:String, title: String, selftext: String, formattedDate: String): Post={
    (subreddit,title,selftext,formattedDate)
  }
}