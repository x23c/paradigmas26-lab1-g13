import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[Models.Subscription] = FileIO.readSubscriptions().getOrElse(List())

    val allPosts: List[(String, String)] = subscriptions.map { case (name, url) =>
      println(s"Fetching posts from: $url")
      
      val posts = FileIO.downloadFeed(url)
      .flatMap(Parser.parseRedditFeed)
      .map(_.filter{ case (_, title, selftext, _, _, _)=> title.nonEmpty && selftext.nonEmpty && selftext.trim.nonEmpty})
      .getOrElse(List())
      //Aca filtre el resultado del parser con la funcion de alto orden filter 
      //y los 3 parametros q puse son en orden para cuando no tiene titulo, no tiene texto y cuando el texto es vacio
      
      // ejercicio 6
      val totalScore = posts.foldLeft(0)((acumulador, post) => acumulador + post._5) 
      // suma todos los scores de los posts de la suscripción, empieza en 0 y va acumulando post._5 que es el score
      
      val top5posts = posts.take(5).map { post => 
        val (_, title, _, date, _, url) = post
        s"Titulo: $title\nFecha: $date\nURL: $url"
        }.mkString("\n\n")  
      // toma los primeros 5 posts y genera un string con solo titulo, fecha y url para cada uno
        
      val texts = posts.map(post => post._2 + " " + post._3) 
      // une titulo y contenido de cada post en un solo string, generando una lista de textos completos
        
      val words = texts.flatMap(WordsAnalyzer.extraerPalabras) 
      // separa cada texto en palabras individuales
        
      val filteredWords = WordsAnalyzer.filtrarPalabrasMayus(words)
      // filtra las palabras para quedarse solo con las que empiezan con mayúscula
        
      val wordFrequencies = WordsAnalyzer.contarFrecuencias(filteredWords)  
      // cuenta cuántas veces aparece cada palabra y devuelve lista ordenada de mayor a menor frecuencia
        
      val top5words = wordFrequencies.take(5).map{ case (w, c) => s"$w: $c" }.mkString(", ") 
      // toma las 5 palabras más frecuentes y genera un string legible con palabra y cantidad
        
      val finalText = 
        s"""
        |Subscription: $name 
        |Total score: $totalScore
        |Top 5 words: $top5words
        |
        |Top 5 posts: 
        |$top5posts

       
        |""".stripMargin

        (name, finalText)
    }

    val output = allPosts
      .map { case (name, text) => Formatters.formatSubscription(name, text) }
      .mkString("\n")

    println(output)
  }
}

/* Ejemplo simple para entender:
Supongamos que tenemos posts así (simplificado):
(id, title, selftext, ..., score)

val posts = List(
  ("1", "Hola", "Mundo", "", 10),
  ("2", "Scala", "es genial", "", 5)
)

Paso 1: unir title + selftext
val textos = posts.map(post => post._2 + " " + post._3)

textos queda:
List("Hola Mundo", "Scala es genial")


Paso 2: separar en palabras y juntar todo
val palabras = textos.flatMap(WordsAnalyzer.extraerPalabras)

palabras queda:
List("Hola", "Mundo", "Scala", "es", "genial")


Resumen:
textos   → frases completas (1 por post)
palabras → todas las palabras juntas en una sola lista */