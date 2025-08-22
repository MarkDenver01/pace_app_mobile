package io.dev.pace_app_mobile.domain.enums


/**
 * Http status
 *
 * @property code
 * @constructor Create empty Http status
 */
enum class HttpStatus(val code: Int) {
    /**
     * Ok
     *
     * @constructor Create empty Ok
     */
    OK(200),

    /**
     * Created
     *
     * @constructor Create empty Created
     */
    CREATED(201),

    /**
     * No Content
     *
     * @constructor Create empty No Content
     */
    NO_CONTENT(204),

    /**
     * Bad Request
     *
     * @constructor Create empty Bad Request
     */
    BAD_REQUEST(400),

    /**
     * Unauthorized
     *
     * @constructor Create empty Unauthorized
     */
    UNAUTHORIZED(401),

    /**
     * Forbidden
     *
     * @constructor Create empty Forbidden
     */
    FORBIDDEN(403),

    /**
     * Not Found
     *
     * @constructor Create empty Not Found
     */
    NOT_FOUND(404),

    /**
     * Conflict
     *
     * @constructor Create empty Conflict
     */
    CONFLICT(409),

    /**
     * Internal Server Error
     *
     * @constructor Create empty Internal Server Error
     */
    INTERNAL_SERVER_ERROR(500),

    /**
     * Service Unavailable
     *
     * @constructor Create empty Service Unavailable
     */
    SERVICE_UNAVAILABLE(503),

    /**
     * Unknown Error
     *
     * @constructor Create empty Unknown Error
     */
    UNKNOWN_ERROR(0);

    companion object  {
        fun fromCode(code: Int): HttpStatus {
            return entries.find { it.code == code } ?: UNKNOWN_ERROR
        }
    }
}