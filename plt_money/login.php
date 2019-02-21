<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='POST'){

	 	$username = $_POST["username"];
	 	$password = $_POST['password'];

		$query = "SELECT * FROM user WHERE username = '$username' AND password = md5('$password');";

		$res = mysqli_query($con, $query);

		$row = mysqli_fetch_array($res);

		if($row != NULL){
			$result = array();
			array_push($result,array(
			 "user_id"=>$row['user_id'],
			 "username"=>$row['username'],
			 "password"=>$row['password'],
			 "status"=>'success'
			 )
			);
			 
			echo json_encode(array("result"=>$result));
			 
			mysqli_close($con);
		}else{
			$result = array();
			array_push($result,array(
			 "user_id"=>0,
			 "username"=>'',
			 "password"=>'',
			 "status"=>'failed'
			 )
			);
			 
			echo json_encode(array("result"=>$result));
		}
	}
?>
