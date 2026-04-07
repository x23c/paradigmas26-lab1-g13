import Models._

object WordsAnalyzer {

  // Lista de stopwords
  val stopwords: Set[String] = Set(
    "the", "about", "above", "after", "again", "against", "all", "am", "an",
    "and", "any", "are", "aren't", "as", "at", "be", "because", "been",
    "before", "being", "below", "between", "both", "but", "by", "can't",
    "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't",
    "doing", "don't", "down", "during", "each", "few", "for", "from", "further",
    "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd",
    "he'll", "he's", "her", "here", "here's", "hers", "herself", "him",
    "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if",
    "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me",
    "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off",
    "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves",
    "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's",
    "should", "shouldn't", "so", "some", "such", "than", "that", "that's",
    "the", "their", "theirs", "them", "themselves", "then", "there", "there's",
    "these", "they", "they'd", "they'll", "they're", "they've", "this", "those",
    "through", "to", "too", "under", "until", "up", "very", "was", "wasn't",
    "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what",
    "what's", "when", "when's", "where", "where's", "which", "while", "who",
    "who's", "whom", "why", "why's", "with", "won't", "would",
    "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
    "yourself", "yourselves"
  )

  // Divide el texto en palabras removiendo caracteres especiales
  def extraerPalabras(texto: String): List[String] = {
    texto
      .split("[\\s\\p{P}]+")  // Divide por espacios y puntuación
      .filter(_.nonEmpty)     // Elimina strings vacíos
      .toList
  }

  // Filtra palabras que comienzan con mayuscula y no estan en stopwords
  def filtrarPalabrasMayus(palabras: List[String]): List[String] = {
    palabras.filter { palabra =>
      // Verifica que comience con mayuscula y no sea vacia
      palabra.nonEmpty && palabra.head.isUpper &&
      // Verifica que no sea stopword comparando en minuscula
      !stopwords.contains(palabra.toLowerCase) &&
      // Verifica que no sea "Titulo", "Contenido", "Fecha" o "Subreddit"
      !Set("Titulo", "Contenido", "Fecha", "Subreddit").contains(palabra)
    }
  }

  // Agrupa palabras equivalentes y cuenta ocurrencias
  def contarFrecuencias(palabras: List[String]): List[PalabraFrequencia] = {
    palabras
      .groupBy(_.toLowerCase)      // groupBy: agrupa por la palabra en minusculas
      .map { case (palabra, grupo) =>
        (grupo.head, grupo.length)  // Usa la forma original del primer elemento
      }
      .toList
      .sortBy(-_._2)              // Ordena por frecuencia descendente
  }

  // Analiza y obtiene las top 5 palabras más frecuentes
  def analyzeTopWords(posts: List[(String, String, String, String, Int, String)]): String = {
    val texts = posts.map(post => post._2 + " " + post._3)
    val words = texts.flatMap(WordsAnalyzer.extraerPalabras)
    val filteredWords = WordsAnalyzer.filtrarPalabrasMayus(words)
    val wordFrequencies = WordsAnalyzer.contarFrecuencias(filteredWords)
    
    wordFrequencies.take(5).map { case (w, c) => s"$w: $c" }.mkString(", ")
  }
}