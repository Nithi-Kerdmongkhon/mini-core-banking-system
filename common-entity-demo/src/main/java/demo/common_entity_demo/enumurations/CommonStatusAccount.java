package demo.common_entity_demo.enumurations;

public enum CommonStatusAccount {

    ACTIVE("Active"),
    INACTIVE("InActive"),
    FROZEN("Frozen"),
    CLOSED("Closed");


    private final String descEn;

    CommonStatusAccount(final String descEn) {
        this.descEn = descEn;
    }
    public String getDescEn() {
        return descEn;
    }

    public static CommonStatusAccount getStatusDescEn(final String value) {
        for (CommonStatusAccount a : values()) {
            if (a.descEn.equalsIgnoreCase(value)) {
                return a;
            }
        }
        return null;
    }

}
