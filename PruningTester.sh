function pause(){
   read -p "$*"
}
echo ""
echo ""
echo "this tester runs 16 trials - half for size 3x3 puzzle and half for 4x4"
echo "for each size it runs 8 trials, 4 for 30 shuffles and 4 for 40 shuffles"
echo "-----------------------------------------------------------------------------------------------------"
echo "WARNING - sometimes even with shuffles of this small size the program will run for a really long time"
echo "WARNING - if something is running too long press 'ctrl + c' or a different kill command"
echo "-----------------------------------------------------------------------------------------------------"
echo "output is printed to a file named 'outputPruningTest.txt"
echo ""
echo ""
pause 'Press [Enter] key to continue...'
for x in 3 4
do
  for s in 30 40
  do
    for t in 1 2 3 4
    do
      echo "starting trial $t/4 for size $x by $x with $s shuffles"
      java MainPruning $x $x $s >> outputPruningTest.txt
      echo "     trial complete"
    done
  done
done
wait