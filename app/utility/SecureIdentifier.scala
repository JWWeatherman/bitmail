package dataentry.utility

import java.security.SecureRandom
import java.util.Base64

import play.api.libs.json._

class SecureIdentifier private (aId : Seq[Byte]) {
  private val id : Seq[Byte] = aId

  override def toString = SecureIdentifier.encoder.encodeToString(id.to[Array])

  override def equals(obj : scala.Any) = super.equals(obj) || (obj match {
    case that : SecureIdentifier => that.id.equals(this.id)
    case _ => false
  })

  override def hashCode() = this.id.hashCode()
}

object SecureIdentifier {
  private val encoder = Base64.getEncoder
  private val decoder = Base64.getDecoder
  private val random = new SecureRandom

  implicit val secureIdentifierFormat = Format[SecureIdentifier](
    new Reads[SecureIdentifier] {
      override def reads(json : JsValue) : JsResult[SecureIdentifier] = json match {
          case JsString(base64String) =>
            try {
              JsSuccess(SecureIdentifier(base64String))
            } catch {
              case e : IllegalArgumentException =>
                JsError(s"Could not parse base64 string, exception: ${e.toString}")
            }
          case _ => JsError("input json is not a string")
        }
    },
    new Writes[SecureIdentifier] {
      override def writes(o : SecureIdentifier) : JsValue = {
        JsString(o.toString)
      }
    }
  )

  def apply(aId : String) : SecureIdentifier = new SecureIdentifier(decoder.decode(aId).toSeq)

  def apply(size : Int) : SecureIdentifier = {
    val buffer = Array.fill[Byte](size)(0)
    random.nextBytes(buffer)
    new SecureIdentifier(buffer.toSeq)
  }
}
