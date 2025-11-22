# 1. You have a list with numbers. Compute the mean of this list.
numbers = [1, 2, 3, 5, 11, 7]
mean = sum(numbers) / len(numbers)
print ("The mean of " + str(numbers) + " is ", mean)

# 2. You have a list of numbers. Create two lists: the list of positives and a list of negatives.
nums = [1, 3, -4, -7, 9, -11, 8, -2]
positives = []
negatives = []
for i in nums:
    if i > 0: 
        positives.append(i)
    elif i < 0:
        negatives.append(i)
print("Positives: ", positives)
print("Negatives: ", negatives)

#  3. You have a list. Compute the sum of the first n-2 elements.
lst = [1, 6, 9, 5, 8, 9, 4, 10, 3]
n = len(lst)
i = 0
# sum = 0
# while i < (n - 2):
#     sum += lst[i]
#     i += 1
sum = sum(lst[:n-2])
print("The sum is: ", sum)

#  4. You have a list. Compute a list that contains all the elements from the odd positions.
odd_position_elems = lst[1::2] # slice from index 1 and take every 2nd element
print("Elements at odd positions: ", odd_position_elems)

#  5. You have a list of numbers. Find the minimum element of the list without using the min function.
minEl = nums[0]
for i in nums:
    if i < minEl: 
        minEl = i
print("The min is: ", minEl)
