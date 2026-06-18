object Main {

  type Subscription = (String, String)  //subreddit name, url
  type Post = (String, String, String, String)  //subreddit, title, selftext, formattedDate
  type PalabraFrequencia = (String, Int) // Tipo para representar frecuencias: (palabra, cantidad) en ejercicio 5

  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[Subscription] = FileIO.readSubscriptions().getOrElse(List())
    
    val allPosts: List[Post] = subscriptions.flatMap { subscription =>
      println(s"Fetching posts from: ${subscription._1}, ${subscription._2}")
      val posts = FileIO.downloadFeed(subscription._2)
      .flatMap {jsonfeed => FileIO.extractPosts(subscription._1, jsonfeed)}
      .getOrElse(List())
      
      posts
    }



    //val postsFiltered = allPosts.filter (post => post._3.trim.nonEmpty && post._2.trim.nonEmpty)  podria ser solo asi pero lo hago con case para que quede mas legible
    val postsFiltered = allPosts.filter{ case (_, title, selftext, _) => title.trim.nonEmpty && selftext.trim.nonEmpty}

    val output = postsFiltered
      .map { case (subreddit, title, selftext, formattedDate) => s"Subreddit: $subreddit\n Title: $title\n Date: $formattedDate\n Content: ${selftext.take(100)}...\n" + ("-" * 80) }
      .mkString("\n")   

    // devuelve algo como Map[subreddit -> List(post1,...,postn)] o sea string -> List[Post]
    val postsBySubreddit = postsFiltered.groupBy(_._1)
      
    // tomo solo List[Post] y se lo paso a extraer palabras (una vez por cada subreddit/string) para que me devuelva
    // una lista de las palabras filtraadas de los posts por cada subreddit 
    val wordsBySubreddit = postsBySubreddit.map{ case (subreddit, listOfPosts) => 
      (subreddit, Analyzer.extraerFiltrarPalabras(listOfPosts))}  
      // map me devuelve un Map con subreddit -> lista de palabras filtradas de cada post
      

    // contar el número de ocurrencias de cada grupo de palabras equivalentes.
    val countWords = wordsBySubreddit.map{ case (subreddit, filteredwords) => 
      (subreddit, Analyzer.contarFrecuencias(filteredwords))}
      // countWords tiene la forma Map[String, List[(String, Int)]] es decir: subreddit, [(palabra, cantidad)]

    println(output) 

    println("\nPalabras mas frecuentes (no stopwords):")
    countWords.foreach { case (subreddit, wordCounts) =>
      println(s"\nSubreddit: $subreddit")
      wordCounts.foreach { case (word, count) =>
        println(s"$word: $count")}
    }
  } 
}

  /*  version antes de hacer Option[List[x]] en FileIO: 
      val allPosts: List[Post] = subscriptions.flatMap { subscription =>
      println(s"Fetching posts from: ${subscription._1}, ${subscription._2}")
      val jsonfeed = FileIO.downloadFeed(subscription._2)
      val posts = FileIO.extractPosts(subscription._1, jsonfeed)

      posts
    }
  */

/*
  val jsonfeed = """ {
  "data": {
    "children": [
      {
        "data": {
          "subreddit": "scala",
          "title": "  ",
          "selftext": "Contenido del primer post",
          "created_utc": 1718753421
        }
      },
      {
        "data": {
          "subreddit": "scala",
          "title": "Segundo post",
          "selftext": "Contenido del segundo post",
          "created_utc": 1718753500
        }
      },
      {
        "data": {
          "subreddit": "scala",
          "title": "Tercer post",
          "selftext": "Contenido del tercer post",
          "created_utc": 1718753600
        }
      }
    ]
  }
} """ */
