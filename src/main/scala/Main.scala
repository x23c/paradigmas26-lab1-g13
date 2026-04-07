import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[Models.Subscription] = FileIO.readSubscriptions().getOrElse(List())

    val allPosts = SubscriptionProcessor.processAllSubscriptions(subscriptions)

    val output = allPosts
      .map { case (name, text) => Formatters.formatSubscription(name, text) }
      .mkString("\n")

    println(output)
  }
}

/* Ejemplo simple para entender():
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