package demo.common_entity_demo.enumurations;

public enum AccountType {
    SAVINGS("savings"),
    CURRENT("current"),
    FIXED_DEPOSIT("fixed_deposit");

    private final String prefix;

    AccountType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public static AccountType mapPrefix(String type){
        if (type == null){
            throw new IllegalArgumentException("Account type is required");
        }
        for (AccountType accountType : values()){
            if (accountType.getPrefix().equalsIgnoreCase(type)){
                return accountType;
            }
        }
        throw new IllegalArgumentException("Invalid account type: " + type);
    }

}
