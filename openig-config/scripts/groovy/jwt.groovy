import java.security.KeyFactory
import org.forgerock.json.jose.builders.JwtBuilderFactory
import org.forgerock.json.jose.jws.SignedJwt
import org.forgerock.json.jose.jws.SigningManager
import org.forgerock.http.protocol.Status
import java.security.spec.X509EncodedKeySpec

//extract jwt from request header
def jwt = request.headers['Authorization']?.firstValue

if (jwt!=null && jwt.startsWith("Bearer eyJ")) {
    jwt=jwt.replace("Bearer ", "")

    try {
        //parse jwt
        def sjwt=new JwtBuilderFactory().reconstruct(jwt, SignedJwt.class)

        //verify jwt signature
        if (!sjwt.verify(new SigningManager().newRsaSigningHandler(getKey(sjwt.getClaimsSet())))) {
            throw new Exception("invalid signature")
        }

        //check jwt expiration
        if ((sjwt.getClaimsSet().getExpirationTime()!=null && sjwt.getClaimsSet().getExpirationTime().before(new Date()))) {
            throw new Exception("signature expired "+sjwt.getClaimsSet().getExpirationTime())
        }

        //check role
         if (!sjwt.getClaimsSet().keys().contains("role") 
            || !allowedRoles.contains(sjwt.getClaimsSet().getClaim("role", String.class))) {
             return getErrorResponse(Status.FORBIDDEN, "Forbidden")            
        }
        attributes["sub"] = sjwt.getClaimsSet().getClaim("sub", String.class)
        attributes["role"] = sjwt.getClaimsSet().getClaim("role", String.class)

        //add name from JWT claim to header
        request.headers.put('X-Auth-Username', sjwt.getClaimsSet().getClaim("name"))

        return next.handle(new org.forgerock.openig.openam.StsContext(context, jwt), request)
    } catch(Exception e) {
        e.printStackTrace();
        return getErrorResponse(Status.UNAUTHORIZED, e.getMessage())
    }
} else {
    //returns 401 status if JWT not present in request
    return getErrorResponse(Status.UNAUTHORIZED, "Not Authenticated")
}

return next.handle(context, request)


def getErrorResponse(status, message) {
    def response = new Response()
    response.status = status
    response.headers['Content-Type'] = "application/json"
    response.setEntity("{'error' : '" + message + "'}")
    return response
}

def getKey(claims) {
    def pem=iss[claims.getIssuer()]
    if (pem != null) {
        def pemReplaced = pem.replaceFirst("(?m)(?s)^---*BEGIN.*---*\$(.*)^---*END.*---*\$.*", "\$1")
        byte[] encoded = Base64.getMimeDecoder().decode(pemReplaced)
        def kf = KeyFactory.getInstance("RSA")
        def pubKey = kf.generatePublic(new X509EncodedKeySpec(encoded))
        println 'got pub key' + pubKey
        return pubKey
    }

    throw new Exception('Unknown issuer')
}
