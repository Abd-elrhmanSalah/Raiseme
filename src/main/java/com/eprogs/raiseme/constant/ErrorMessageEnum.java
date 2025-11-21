package com.eprogs.raiseme.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessageEnum {
    ERROR_USER_LOCKED("USER_LOCKED", "This user is locked", "هذا الحساب غير مفعل"),
    ERROR_USER_NOT_ACTIVE("USER_NOT_ACTIVE", "This user is active yet", "لم يتم تنشيط هذا الحساب بعد"),
    ERROR_WRONG_OTP("ERROR_WRONG_OTP", "OTP not correct", "OTP غير صحيح"),
    ERROR_OTP_EXPIRED("ERROR_OTP_EXPIRED", "OTP expired", "OTP منتهى الصلاحية"),
    ERROR_NOTLOGGEDIN("ERROR_AUTHENTICATION_FAILED", "Please login first", "برجاء تسجيل الدخول أولا"),
    ERROR_PASSWORD_USERID("ERROR_PASSWORD_USERID", "New password can't be the same user email", "لا يمكن ان يكون الرقم السرى نفس البريد الالكترونى للمستخدم"),
    ERROR_NO_TOKEN_SUPPORTED("ERROR_AUTHENTICATION_FAILED", "Token not provided", "لا يوجد توكن"),
    ERROR_AUTHENTICATION_FAILED("ERROR_AUTHENTICATION_FAILED", "Username or password not correct", "اسم المستخدم او الرقم السرى غير صحيح"),
    ERROR_UNAUTHORIZED("ERROR_UNAUTHORIZED", "You are not authorized to do this action", "انت غير مصرح لك بالقيام بهذا الاجراء"),
    ERROR_EMAIL_ALREADY_EXIST("EMAIL_ALREADY_EXIST", "Email already exist", "هذا الايميل موجود بالفعل"),
    ERROR_ITEM_NOT_FOUND("Record Not Found", "Record Not Found", "هذا السجل غير موجود"),
    ERROR_UPLOAD_FILE("ERROR_UPLOAD_FILE", "Something wrong in uploading file", "يوجد مشكلة فى رفع الملف"),
    ERROR_FILE_NOT_FOUND("ERROR_FILE_NOT_FOUND", "No file with this name", "لا يوجد ملف بهذا الاسم"),
    ERROR_FORBIDDEN("ERROR_FORBIDDEN", "User does not have the permission to access this resource", "المستخدم ليس لديه الصلاحية للوصول الى هذا المورد"),
    ERROR_NOT_ALLOWED("ERROR_NOT_ALLOWED", "NOT ALLOWED", "غير مسموح");

    private final String key;
    private final String messageEN;
    private final String messageAR;
}
