package javax.mail;

public interface QuotaAwareStore {
    Quota[] getQuota(String str) throws MessagingException;

    void setQuota(Quota quota) throws MessagingException;
}
