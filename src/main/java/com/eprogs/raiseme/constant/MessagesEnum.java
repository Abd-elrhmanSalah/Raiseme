package com.eprogs.raiseme.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessagesEnum {

    CREATED(1L, "CREATED_SUCCESS", "Entered Successfully", "تم الادخال بنجاح"),
    UPDATED(2L, "UPDATED_SUCCESS", "Updated Successfully", "تم التعديل بنجاح"),
    DELETED(3L, "DELETED_SUCCESS", "Deleted Successfully", "تم الحذف بنجاح"),
    EMAIL_SENT(4L, "SENT_SUCCESS", "Sent Successfully", "تم الارسال بنجاح"),
    LOGOUT(5L, "LOGOUT_SUCCESS", "Logout Successfully", "تم تسجيل الخروج بنجاح"),
    REACTIVATED(6L, "REACTIVATED_SUCCESS", "Reactivated Successfully", "تم إعادة التفعيل بنجاح"),
    ACTIVATED(6L, "ACTIVATED", "Activated Successfully", "تم التفعيل بنجاح"),
    DEACTIVATED(7L, "DEACTIVATED_SUCCESS", "Deactivated Successfully", "تم التعطيل بنجاح"),
    FORBIDDEN(8L, "FORBIDDEN", "Forbidden", "غير مسموح"),
    MAIL_CHECKED_SUCCESSFULLY(9L, "MAIL_CHECKED_SUCCESSFULLY", "MAIL configuration checked successfully", "تم التحقق من اعدادت الميل بنجاح"),
    MAIL_CHECKED_FAILURE(10L, "MAIL_CHECKED_FAILURE", "mail configuration not correct", " اعدادت الميل غير صحيحة"),
    UPLOADED(11L, "UPLOADED", "UPLOADED Successfully", "تم رفع الملف بنجاح"),


    ;
    private final Long code;
    private final String key;
    private final String messageEN;
    private final String messageAR;

    public static MessagesEnum fromKey(String key) {
        for (MessagesEnum msg : values()) {
            if (msg.getKey().equalsIgnoreCase(key)) {
                return msg;
            }
        }
        return null;
    }
}
