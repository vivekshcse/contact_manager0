console.log("this is js file..");


const toggleSidebar=()=>{
	
	
	if($(".sidbar").is(":visible")){
		//true
		//need to off
		
		$(".sidbar").css("display","none");
		$(".content").css("margin-left","0%");
	}else{
		//false
		//need to on
		
		$(".sidbar").css("display","block");
		$(".content").css("margin-left","20%");
	}
}