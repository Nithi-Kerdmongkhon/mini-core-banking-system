package demo.common_entity_demo.enumurations;

public enum CommonStatus {

    SUCCESS("0000", "success", "ทำรายการสำเร็จ"),

    NOT_FOUND_FNAME_AND_LNAME("0001", "Not found first name and last name", "ไม่พบชื่อ"),
    INVALID_EMAIL_FORMAT("0002", "Invalid email format", "Email ไม่ถูกต้องตามรูปแบบ"),
    EMAIL_IS_ALREADY("0003", "This email is already in the system.", "มี email นี้ในระบบแล้ว"),
    NOT_FOUND_CUSTOMER("0004", "Not found customer.", "ไม่พบเจ้าของบัญชี"),
    INVALID_ACCOUNT_TYPE("0005", "invalid account type", "ประเภทบัญชีไม่ถูกต้อง"),
    INVALID_BALANCE("0005", "invalid balance", "จำนวนเงินไม่ถูกต้อง"),
    INVALID_REQUEST("0006","invalid request","คำขอไม่ถูกต้อง"),
    NOT_FOUND_ACCOUNT("0007", "Not found account.", "ไม่พบบัญชี"),
    ACCOUNT_IS_NULL_OR_EMPTY("0008","Account Is null or Empty","บัญชี ไม่ม่มีค่า หรือ ว่าง"),
    THIS_ACCOUNT_UNABLE_TO_DEPOSIT_MONEY("0009","Unable to deposit money","บัญชีนี้ไม่สามารถฝากเงินได้"),
    AMOUNT_IS_NULL("0010","Amount is null","จำนวนเงินเป็นค่าว่าง"),
    AMOUNT_MUST_BE_GREATER_THAN_ZERO("0011","Amount must be greater than zero","จำนวนเงินต้องมากกว่าศูนย์"),
    AMOUNT_INVALID_PRECISION("0012","Amount invalid precision","จำนวนทศนิยมไม่ถูกต้อง"),
    ERROR_EXCEPTION_OCCURRED("9999", "system error occurred", "ระบบเกิดเหตุขัดข้อง กรุณาลองทำรายการใหม่ภายหลัง");

    private final String code;
    private final String descEn;
    private final String descTh;

    CommonStatus(final String code, final String descEn, final String descTh) {
        this.code = code;
        this.descEn = descEn;
        this.descTh = descTh;
    }

    public String getDescTh() {
        return descTh;
    }

    public String getCode() {
        return code;
    }

    public String getDescEn() {
        return descEn;
    }

    public static CommonStatus getStatusCode(final String value) {
        for (CommonStatus a : values()) {
            if (a.code.equalsIgnoreCase(value)) {
                return a;
            }
        }
        return null;
    }

    public static CommonStatus getStatusDescEn(final String value) {
        for (CommonStatus a : values()) {
            if (a.descEn.equalsIgnoreCase(value)) {
                return a;
            }
        }
        return null;
    }
}
