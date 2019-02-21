<?php
 	
 	require "db_config.php";

 	$category_id = $_GET['category_id'];

	$query = "SELECT * FROM category WHERE category_id = $category_id;";

	$res = mysqli_query($con, $query);

	$row = mysqli_fetch_array($res);

	if($row != NULL){
		$result = array();
		array_push($result,array(
		 "category_id"=>$row['category_id'],
		 "category_name"=>$row['category_name'],
		 "status"=>'success'
		 )
		);
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	}else{
		echo 'No available categories!';
	}
?>
