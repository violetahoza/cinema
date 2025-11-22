# 1.  You have a dictionary which gives you the price for some products. You also have a shopping
# list: for each item you know the quantity. Compute the price of your shopping list. If it is possible, avoid using for or while, remember that
# there are list comprehension, access of values based on the key, and sum of a list.
price = {"apples": 10, "milk": 12, "bread": 5}
myList = [("apples", 2), ("milk", 2)]
totalPrice = sum(price[item] * quantity for item, quantity in myList)
print("Total price of the shopping list: ", totalPrice)

# 2.  You have a list of friends that gave you money in your last trip. Extract their names (without
# duplicates) by using sets
friends = [("andrew" ,10) , ("george" ,20) , ("andrew" , 5), ("ann", 10)]
unique_names = {friend[0] for friend in friends}
print("Unique names of friends:", unique_names)

# 3. Take an integer as standard input. List all numbers up to that number if the number is even
# and all numbers up to that number, only squared, if the number is odd.
n = int(input("Enter an integer: "))
if n % 2 == 0:
    result = list(range(n + 1))
else:
    result = [i ** 2 for i in range(n + 1)]
print("Result: ", result)

# 4. Take a string, s in and a positive integer, num in as standard input. Create a dictionary that will
# have all numbers up to num in as keys and all string characters from the corresponding num in
# positions as values.
s = input("Enter a string: ")
num = int(input("Enter a positive number: "))
result_dict = {i: s[i] for i in range(min(num, len(s)))}
print("Resulting dictionary: ", result_dict)

# 5. Define a function with a single parameter (a string) that will return a new string: for each letter
# in the original string, the new string will contain the corresponding next letter in the alphabet.
def next_letter(string):
    new_string = ''.join(chr(ord(char) + 1) for char in string)
    return  new_string
result1 = next_letter("hello")
print("Original string:", "hello")
print("Next letter string:", result1)

# 6. Write a program to open 2 .txt files at the same time. For the first one convert the first letter of
# every word to upper case and then add the file’s content to the other file.
def capitalize_and_append(file1_path, file2_path):
    try:
        # Open the first file in read mode and the second file in append mode
        with open(file1_path, 'r') as file1, open(file2_path, 'a') as file2:
            # Read the content of the first file
            content = file1.read()
            
            # Capitalize the first letter of each word
            modified_content = ' '.join(word.capitalize() for word in content.split())
            
            # Append the modified content to the second file
            file2.write(modified_content + '\n')  # Adding a newline for separation

        print(f"Modified content from '{file1_path}' has been appended to '{file2_path}'.")

    except FileNotFoundError as e:
        print(f"Error: {e}")

# Example usage
file1_path = 'input.txt'  # Replace with the path to your first file
file2_path = 'output.txt'  # Replace with the path to your second file
capitalize_and_append(file1_path, file2_path)
