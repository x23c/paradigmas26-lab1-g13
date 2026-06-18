
object Analyzer {
    val stopwords = Set("the", "about", "above", "after", "again", "against", "all", "am", "an",
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
    "these", "they", "they'd", "they'll", "re", "they've", "this", "those",
    "through", "to", "too", "under", "until", "up", "very", "was", "wasn't",
    "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what",
    "what's", "when", "when's", "where", "where's", "which", "while", "who",
    "who's", "whom", "why", "why's", "with", "won't", "would",
    "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
    "yourself", "yourselves")

    // recibe todos los posts de un subreddit (obtenidos por groupBy en Main) y devuelve una lista con cada palabra del selftext de cada post. Luego filtra dejando en la lista solo las palabras que cumplen con el enunciado
    def extraerFiltrarPalabras (listOfPosts : List[Main.Post]) : List[String] = { 
        listOfPosts.flatMap(posts => posts._3.split("[\\s\\p{P}]+"))  // Divide por espacios y puntuación 
        .filter (word => word.nonEmpty && 
                         word.head.isUpper &&        // Verifica que comience con mayuscula y no sea vacia
                         word.length > 1 &&    
                         !stopwords.contains(word.toLowerCase))   // Verifica que no sea stopword comparando en minuscula
    }
    
    def contarFrecuencias(words: List[String]): List[Main.PalabraFrequencia] = {
    words
      .groupBy(_.toLowerCase)      // groupBy: agrupa por la palabra en minusculas
      .map { case (word, group) =>
        (group.head, group.length)  // (palabra, cantidad de ocurrencias de esa palabra)
      }
      .toList
      .sortBy(-_._2)              // Ordena por frecuencia descendente
  }
}