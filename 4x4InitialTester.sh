function pause(){
   read -p "$*"
}
echo ""
echo ""
echo "this tester runs 4x4 trials - half will be with 30 shuffles half  with 40"
echo "-----------------------------------------------------------------------------------------------------"
echo "WARNING - sometimes even with shuffles of this small size the program will run for a really long time"
echo "WARNING - if something is running too long press 'ctrl + c' or a different kill command"
echo "-----------------------------------------------------------------------------------------------------"
echo "output is printed to a file named 'initialOutput.txt"
echo ""
echo ""
pause 'Press [Enter] key to continue...'
for x in 4
do
  for s in 30 40
  do
    for t in 1 2 3 4 5 6 7 8 9 10
    do
      echo "starting trial $t/10 for size $x by $x with $s shuffles"
      java Main $x $x $s >> 4x4output.txt
      echo "     trial complete"
    done
  done
done
wait
