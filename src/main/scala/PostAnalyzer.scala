

object PostAnalyzer {

  // Calcula el score total de todos los posts
  def calculateTotalScore(posts: List[(String, String, String, String, Int, String)]): Int = {
    posts.foldLeft(0)((acumulador, post) => acumulador + post._5)
  }

}