import numpy as np

def findAll(letter, word):
  a = 0
  for i in range(len(word)):
    if word[i] == letter:
      a += 1

  return a

# f = open("4 letters csv.csv")
# f = open("5 letters csv.csv")
f = open("6 letters csv.csv")
words = []
valid = False
for i in f:
  for c in list(map(chr,range(ord('a'),ord('z')+1))):
    num = findAll(c, i)
    if num > 1:
      valid = False
      break
    else:
      valid = True
  if valid:
    words.append(i)

# new_array = np.array(words)
# file = open("4 letters csv2.csv", "w+")
# file = open("5 letters csv2.csv", "w+")
file = open("6 letters csv2.csv", "w+")
# content = str(new_array)
for word in words:
  file.write(word)
file.close()
