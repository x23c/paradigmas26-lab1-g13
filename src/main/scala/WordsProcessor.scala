import Models._

object WordsProcessor {

  // Extrae las palabras principales (capitalizadas) y sus frecuencias de una lista de posts
  def analyzeWordFrequencies(posts: List[Post]): List[Models.PalabraFrequencia] = {
    val texts = extractPostTexts(posts)
    val words = texts.flatMap(WordsAnalyzer.extraerPalabras)
    val filteredWords = WordsAnalyzer.filtrarPalabrasMayus(words)
    WordsAnalyzer.contarFrecuencias(filteredWords)
  }

  // Obtiene los 5 términos más frecuentes con formato legible
  def getTop5Words(wordFrequencies: List[Models.PalabraFrequencia]): String = {
    wordFrequencies.take(5).map { case (w, c) => s"$w: $c" }.mkString(", ")
  }

  // Une el título y contenido de cada post en un solo string
  def extractPostTexts(posts: List[Post]): List[String] = {
    posts.map(post => post._2 + " " + post._3)
  }
}
