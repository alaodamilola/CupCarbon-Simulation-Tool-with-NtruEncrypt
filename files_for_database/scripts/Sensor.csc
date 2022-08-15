getpos p
atget id id
loop
wait
read m
rdata $m rid type froid tarid round
if($type==A)
	if($tarid==$id)
		mark 1
		areadsensor v
		print $v
		rdata $v s s1 v1 s2 v2 s3 v3 s4 v4 s5 v5 s6 v6 end
		data d $id B $id $froid $p $round
		if($s==X)
			data var 0
		end
		if($s==0)
			data var 0
		end
		if($s==1)
			data var 1 $s1 $v1
		end
		if($s==2)
			data var 2 $s1 $v1 $s2 $v2
		end
		if($s==3)
			data var 3 $s1 $v1 $s2 $v2 $s3 $v3
		end
		if($s==4)
			data var 4 $s1 $v1 $s2 $v2 $s3 $v3 $s4 $v4
		end
		if($s==5)
			data var 5 $s1 $v1 $s2 $v2 $s3 $v3 $s4 $v4 $s5 $v5
		end
		if($s==6)
			data var 6 $s1 $v1 $s2 $v2 $s3 $v3 $s4 $v4 $s5 $v5 $s6 $v6
		end
		data d $d $var
		data d $d E
		send $d $rid
	end
end