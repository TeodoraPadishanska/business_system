import { updateCartPrice, checkLoginStatusNavBtn, checkAdminStatusNavBtn} from "./common.js";

updateCartPrice();
checkLoginStatusNavBtn();
checkAdminStatusNavBtn();

const token = localStorage.getItem("token");

async function showProfileName(){
    await fetch("http://localhost:8080/business/user/profile/name",{
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then((response) => {})

    //TODO да показва име вместо профил
    const profileName = document.getElementById("profileName");

}


const logoutBtn = document.getElementById("logout-btn");
logoutBtn.addEventListener("click", async ()=>{
    await fetch("http://localhost:8080/business/users/logout", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    )
    window.location.href ="../index.html";
})