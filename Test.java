import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class EncryptedDataSearch {
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static SecretKey secretKey;

    public static void main(String[] args) throws Exception {
        // Generate a secret key for encryption
        secretKey = generateKey();

        // Original data
        Map<String, String> data = new HashMap<>();
        data.put("1", "Java programming is fun");
        data.put("2", "Encryption is crucial for security");
        data.put("3", "Searchable encryption is advanced");

        // Create encrypted data and searchable index
        Map<String, String> encryptedData = new HashMap<>();
        Map<String, List<String>> index = new HashMap<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String id = entry.getKey();
            String text = entry.getValue();
            String encryptedText = encrypt(text, secretKey);
            encryptedData.put(id, encryptedText);
            buildIndex(id, text, index);
        }

        // Search for a keyword
        String keyword = "encryption";
        System.out.println("Searching for: " + keyword);
        List<String> results = search(keyword, index, encryptedData);
        System.out.println("Search results:");
        results.forEach(System.out::println);
    }

    // Generate a symmetric key
    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGen.init(128); // AES key size
        return keyGen.generateKey();
    }

    // Encrypt a plain text
    private static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Build a searchable index (maps keywords to document IDs)
    private static void buildIndex(String id, String text, Map<String, List<String>> index) {
        String[] words = text.toLowerCase().split("\\s+");
        for (String word : words) {
            index.computeIfAbsent(word, k -> new ArrayList<>()).add(id);
        }
    }

    // Search for a keyword in the index and retrieve corresponding encrypted data
    private static List<String> search(String keyword, Map<String, List<String>> index, Map<String, String> encryptedData) {
        List<String> resultIds = index.getOrDefault(keyword.toLowerCase(), Collections.emptyList());
        List<String> results = new ArrayList<>();
        for (String id : resultIds) {
            results.add(encryptedData.get(id));
        }
        return results;
    }
}

