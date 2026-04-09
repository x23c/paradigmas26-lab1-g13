import models.Types._
object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[Models.Subscription] = FileIO.readSubscriptions() // lista [(nombre,url)]

//proceso cada suscripcion de la lista
    val allPosts: List[(String, String)] = subscriptions.map { case (name,url) =>
      println(s"Fetching posts from: $url")


      val stringFeed = FileIO.downloadFeed(url) //pasa el feed de formato XML a string
 
 
 //parseo el string anterior,, creo una lista de posts
//uso filter para ssacarle los posts vacios
      val posts = Parser.parseRedditFeed(stringFeed).filter{
        case(_,title,selftext,_)=>
        title.nonEmpty && selftext.nonEmpty && selftext.trim.nonEmpty

      }
      (url,posts.map(Formatters.formatPost).mkString("\n\n")) //(url,posts_string)
    }
    //allPosts devuelve List[("url","post1"),...,-("url","postn")]

//convierto cada suscripcion en un bloque de texto
    val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")  //mkstring junta todo

    println(output)
  }
}
