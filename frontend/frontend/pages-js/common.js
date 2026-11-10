export async function getCartPrice(){
    const token = localStorage.getItem("token");
    if(token) {

        const cartTotalRes = await fetch(`http://localhost:8080/business/cart/value`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        });

        let cartPrice = await cartTotalRes.json();
        console.log(cartPrice);
        return cartPrice.toFixed(2);
    }
}

export async function updateCartPrice(){
    const cartTotalEuro = document.getElementById("cart-total-euro");
    const euroTotal = await getCartPrice();
    cartTotalEuro.textContent = `${euroTotal ? euroTotal : 0.00.toFixed(2)} €`;
}




export async function checkLoginStatusNavBtn(){
    const token = localStorage.getItem("token");
    console.log(token);

    if(token){
        fetch("http://localhost:8080/business/users/status", {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }).then(data => {
            const profileBtn = document.getElementById("profileBtn");
            const loginBtn = document.getElementById("loginBtn");


            // console.log(response.then(responseJson => { responseJson.status }));
            if (data.status === 200) {
                console.log("Logged in");
                profileBtn.style.display = "block";
                loginBtn.style.display = "none";
            }
            else{
                console.log("Not Logged in");
                profileBtn.style.display = "none";
                loginBtn.style.display = "block";
                localStorage.removeItem("token");
            }
        })
    }
}

export async function checkAdminStatusNavBtn(){

    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8080/business/users/me", {
        method: 'GET',
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })

    if (response.ok) {
        const data = await response.json();
        if(data.isAdmin){
            const adminLi = document.createElement("li");
            adminLi.classList.add("nav-item");

            const adminLink = document.createElement("a");
            adminLink.href = "/pages/admin.html";
            adminLink.textContent = "Admin";
            adminLink.classList.add("colour-text", "mt-3", "nav-link");

            adminLi.appendChild(adminLink);
            const navBtns = document.getElementById("nav-btns");

            navBtns.insertBefore(adminLi, navBtns.lastElementChild);

        }
    }
}

export function checkBackendHealth(){
    // TODO: go to offline nad prevent further script execution if backend is offline
    try{
        fetch("http://localhost:8080/business/health").then(data => {data.json()});

    }
    catch(error){
        window.location.href = "/offline.html";

    }

}



export function storeOrderViewId(id){
    localStorage.setItem("orderViewId", id);
}

export function getOrderViewId(){
    return localStorage.getItem("orderViewId");
}

