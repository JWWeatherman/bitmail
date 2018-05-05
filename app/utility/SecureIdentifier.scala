package dataentry.utility

import java.security.SecureRandom
import java.util.Base64

import play.api.libs.json._

class SecureIdentifier private (aId : Array[Byte]) {
  private val id : Array[Byte] = aId

  override def toString = SecureIdentifier.encoder.encodeToString(id)
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

  def apply(aId : String) : SecureIdentifier = new SecureIdentifier(decoder.decode(aId))

  def apply(size : Int) : SecureIdentifier = {
    val buffer = Array.fill[Byte](size)(0)
    random.nextBytes(buffer)
    new SecureIdentifier(buffer)
  }
}
