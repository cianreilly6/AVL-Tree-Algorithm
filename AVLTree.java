import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class AVLNode {
  String key;
  String value;
  int height;
  AVLNode left;
  AVLNode right;

  // Constructor to initialize the AVL node
  AVLNode(String key, String value) {
    this.key = key;
    this.value = value;
    height = 1;
  }
}

public class AVLTree {
  AVLNode root;

  // Function to calculate the height of a node
  int height(AVLNode node) {
    if (node == null)
      return 0;
    return node.height;
  }

  // Function to return the maximum of two integers
  int max(int a, int b) {
    return (a > b) ? a : b;
  }

  // Function to get the balance factor of a node
  int getBalance(AVLNode node) {
    if (node == null)
      return 0;
    return height(node.left) - height(node.right);
  }

  // Right rotation operation
  AVLNode rightRotate(AVLNode y) {
    AVLNode x = y.left;
    AVLNode T2 = x.right;

    // Perform rotation
    x.right = y;
    y.left = T2;

    // Update heights
    y.height = max(height(y.left), height(y.right)) + 1;
    x.height = max(height(x.left), height(x.right)) + 1;

    return x;
  }

  // Left rotation operation
  AVLNode leftRotate(AVLNode x) {
    AVLNode y = x.right;
    AVLNode T2 = y.left;

    // Perform rotation
    y.left = x;
    x.right = T2;

    // Update heights
    x.height = max(height(x.left), height(x.right)) + 1;
    y.height = max(height(y.left), height(y.right)) + 1;

    return y;
  }

  // Function to insert a node into the AVL tree
  AVLNode insert(AVLNode node, String key, String value) {
    if (node == null)
      return (new AVLNode(key, value));

    // Perform normal BST insertion
    if (key.compareTo(node.key) < 0)
      node.left = insert(node.left, key, value);
    else if (key.compareTo(node.key) > 0)
      node.right = insert(node.right, key, value);
    else
      return node; // Duplicate keys are not allowed in AVL trees

    // Update height of this ancestor node
    node.height = 1 + max(height(node.left), height(node.right));

    // Get the balance factor of this ancestor node to check whether it became unbalanced
    int balance = getBalance(node);

    // If the node becomes unbalanced, there are four cases

    // Left Left Case
    if (balance > 1 && key.compareTo(node.left.key) < 0)
      return rightRotate(node);

    // Right Right Case
    if (balance < -1 && key.compareTo(node.right.key) > 0)
      return leftRotate(node);

    // Left Right Case
    if (balance > 1 && key.compareTo(node.left.key) > 0) {
      node.left = leftRotate(node.left);
      return rightRotate(node);
    }

    // Right Left Case
    if (balance < -1 && key.compareTo(node.right.key) < 0) {
      node.right = rightRotate(node.right);
      return leftRotate(node);
    }

    // If the node is balanced, return it unchanged
    return node;
  }

  // Function to search for a key in the AVL tree and return its corresponding value
  String search(AVLNode node, String key) {
    if (node == null)
      return null;

    // Traverse the tree to find the node with the given key
    if (key.compareTo(node.key) < 0)
      return search(node.left, key);
    else if (key.compareTo(node.key) > 0)
      return search(node.right, key);
    else
      return node.value; // Return the value if the key is found
  }

  // Function to find the height of the AVL tree
  int findHeight(AVLNode node) {
    if (node == null)
      return 0;
    return Math.max(findHeight(node.left), findHeight(node.right)) + 1;
  }

  // Main method
  public static void main(String[] args) {
    AVLTree tree = new AVLTree();
    String csvFile = "EnglishSpanish (1).csv";
    String line = "";
    String cvsSplitBy = ",";

    // Read the CSV file and insert its contents into the AVL tree
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      while ((line = br.readLine()) != null) {
        String[] pair = line.split(cvsSplitBy);
        // Insert key-value pairs into the AVL tree, trimming keys and converting them to lowercase
        tree.root = tree.insert(tree.root, pair[0].trim().toLowerCase(), pair[1]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Print the height of the AVL tree
    int height = tree.findHeight(tree.root);
    System.out.println("Height of the AVL tree: " + height);

    // Test the translation
    String englishText = "The quick brown fox jump over the lazy dog";
    String[] words = englishText.toLowerCase().split("\\s+");
    StringBuilder spanishText = new StringBuilder();

    // Translate each word in the English text into Spanish
    for (String word : words) {
      word = word.replaceAll("[^a-zA-Z0-9]", ""); // Remove non-alphanumeric characters from the word
      String translation = tree.search(tree.root, word);
      if (translation != null) {
        spanishText.append(translation).append(" "); // Append the translated word to the Spanish text
      } else {
        spanishText.append(word).append(" "); // If the word is not found, append it as it is
      }
    }

    // Print the translated Spanish text
    System.out.println("Spanish translation: " + spanishText.toString().trim());
  }
}
