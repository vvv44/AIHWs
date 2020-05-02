import numpy as np
import csv
import pandas as pd

class DecisionTreeNode(object):
    # Constructor
    def __init__(self, att, thr, left, right):  
        self.attribute = att
        self.threshold = thr
        # left and right are either binary classifications or references to 
        # decision tree nodes
        self.left = left     
        self.right = right   

    def print_tree(self,indent=''):
        # If prints the right subtree, corresponding to the condition x[attribute] > threshold
        # above the condition stored in the node 
        if self.right  in [0,1]:
            print(indent+'       ','class=',self.right)
        else:
            self.right.print_tree(indent+'    ')
            
        print(indent,'if x['+str(self.attribute)+'] <=',self.threshold)
        
        if self.left  in [0,1]:
            print(indent+'       ','class=',self.left)
        else:
            self.left.print_tree(indent+'    ')
        

class DecisionTreeClassifier(object):
    # Constructor
    def __init__(self, max_depth=10, min_samples_split=10, min_accuracy =1):  
        self.max_depth = max_depth
        self.min_samples_split = min_samples_split
        self.min_accuracy = min_accuracy
        
    def fit(self,x,y):  
        self.root = self._id3(x,y,depth=0)
        
    def predict(self,x_test):
        pred = np.zeros(len(x_test),dtype=int)
        for i in range(len(x_test)):
            pred[i] = self._classify(self.root,x_test[i])
        return pred
     
#this method checks several thresholds per attribute and chooses the one with highest information gain      
    def _best_treshold_generate(self,x,y, orig_entropy):
        best_thrs = np.zeros(x.shape[1])
        best_gains = np.zeros(x.shape[1])
        for i in range(x.shape[1]):
#We first generate several random values for an attribute
            thr = np.random.uniform(min(x[:,i]),max(x[:,i]),x.shape[1])
            entropy_thr = np.zeros(thr.shape[0])
#We now check the entropy to get info gain of each attribute and threshold
            for j in range(thr.shape[0]):
                less = x[:,i] <= thr[j]
                more = ~ less
                entropy_thr[j] = self._entropy(y[less], y[more])
#Get the gains of the attribute and choose the threshold that yields the best gain
            gains = orig_entropy - entropy_thr
            best_gains[i] = np.max(gains)
            best_thrs[i] = thr[np.argmax(gains)]
        best_att = np.argmax(best_gains)
#Return a tuple containing the index of the best attribute, and the best threshold (from that attribute)
        return (best_att, best_thrs[best_att])
        
            
    def _id3(self,x,y,depth):
        if y.size==0:
            return
        mean_val = np.mean(y) #check for possible empty y array
        if depth >= self.max_depth or len(y) <= self.min_samples_split or max([mean_val,1-mean_val])>=self.min_accuracy:
            return int(round(mean_val))
        orig_entropy = self._entropy(y, [])
        
#        thr = np.mean(x,axis=0)
#        entropy_attribute = np.zeros(len(thr))
#        for i in range(x.shape[1]):
#            less = x[:,i] <= thr[i]
#            more = ~ less
#            entropy_attribute[i] = self._entropy(y[less], y[more])
#        gain = orig_entropy - entropy_attribute
#            
#        #print('Gain:',gain)
#        best_att = np.argmax(gain)
        
        best_att_thr = self._best_treshold_generate(x,y,orig_entropy)  
#We extract the treshold from the second "row" and attribute from first row
        best_thr = best_att_thr[1]
#We extract the best attribute index
        best_att = best_att_thr[0]
#We proceed to divide values
        less = x[:,best_att] <= best_thr
        more = ~ less
#Will have to divide x and y into xl, yl and xr, yr to give to the node, so that it gives a reference to a decision tree
        return DecisionTreeNode(best_att, best_thr, self._id3(x[less],y[less],depth+1), self._id3(x[more],y[more],depth+1))
      
    def _entropy(self,l,m):
        ent = 0
        for p in [l,m]:
            if len(p)>0:
                pp = sum(p)/len(p)
                pn = 1 -pp
                if pp<1 and pp>0:
                    ent -= len(p)*(pp*np.log2(pp)+pn*np.log2(pn))
        ent = ent/(len(l)+len(m))
        return ent   
    
    def _classify(self, dt_node, x):
        if dt_node in [0,1]:
            return dt_node
        if x[dt_node.attribute] <= dt_node.threshold:
            return self._classify(dt_node.left, x)
        else:
            return self._classify(dt_node.right, x)
    
    def display(self):
        print('Model:')
        self.root.print_tree()

def processCSV():
    with open('placement_data.csv') as file:
        read = csv.reader(file)
        lines = list(read)
        for row in lines:
            if(row[1]=='M'):
                row[1] = 1
            else:
                row[1] = 0
            del row[3]
            del row[4]
            del row[4]
            del row[5]
            if(row[5]=='No'):
                row[5] = 0
            else:
                row[5] = 1
            del row[7]
            del row[9]
            if(row[8]== 'Placed'):
                row[8] = 1
            else:
                row[8] = 0
        del lines[0]
        writer = csv.writer(open('placement_data_cleaned.csv', 'w'))
        writer.writerows(lines)

def processIncomeCSV():
        with open('income_evaluation.csv') as file:
            read = csv.reader(file)
            lines = list(read)
            for row in lines:
                if(row[9]==' Male' or row[9]=='Male'):
                    row[9] = 1
                else:
                    row[9] = 0
                del row[1]
                del row[1]
                del row[1]
                del row[2]
                del row[2]
                del row[2]
                del row[4]
                del row[4]
                del row[5]
                if(row[5] == " <=50K" or row[5] == "<=50K"):
                    row[5] = 0
                else:
                    row[5] = 1
                #we make white be one and other be 0
                if(row[2] == " White" or row[2] == "White"):
                    row[2] = 1
                else:
                    row[2] = 0
            del lines[0]
        writer = csv.writer(open('income_evaluation_cleansed.csv', 'w',newline=''))
        writer.writerows(lines)


#Code for the first dataset
# with open('placement_data_cleaned.csv') as file:
#     read = csv.reader(file)
#     lines = list(read)
#     x = np.array(lines).astype(np.float)
#     y = x[:,8]
#     x = np.delete(x,-1,axis=1)
#     x = np.delete(x,0,axis = 1)

#code for the second dataset
with open('income_evaluation_cleansed.csv') as file:
    read = csv.reader(file)
    lines = list(read)
    x = np.array(lines).astype(np.float)
    y = x[:,5]
    x = np.delete(x,-1,axis = 1)


#Split data into training and testing
ind = np.random.permutation(len(y))
split_ind = int(len(y)*0.8)
x_train = x[ind[:split_ind]]
x_test = x[ind[split_ind:]]
y_train = y[ind[:split_ind]]
y_test = y[ind[split_ind:]]

model = DecisionTreeClassifier()
model.fit(x_train, y_train)

train_pred = model.predict(x_train)
test_pred = model.predict(x_test)

train_acc = np.sum(train_pred==y_train)/len(train_pred)
print('train accuracy:', train_acc)
test_acc = np.sum(test_pred==y_test)/len(test_pred)
print('test accuracy:', test_acc)




#Prun the tree, by getting rid of unnecessary comparisons(nodes), collapsing those 
#which children classify to the same number. (Done for decision trees)
def post_prunning_decision_tree(root):
    if isinstance(root,int):
        return root
    #Do left subtree then right subtree
    root.left = post_prunning_decision_tree(root.left)
    root.right = post_prunning_decision_tree(root.right)
    #if classifications are same
    if root.left==root.right:
        #Give value to substitute the root for
        new_root = root.left
        temp = root
        root = None
        del(temp)
        return new_root
    else:
        return root


post_prunning_decision_tree(model.root)
model.display()