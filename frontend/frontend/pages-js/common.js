export async function getCartPrice(){
    const token = localStorage.getItem("token");
    if(token) {

        const cartTotalRes = await fetch(`http://localhost:8080/business/cart/value`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        });

        return await cartTotalRes.json();
    }
}

export async function updateCartPrice(){

    const cartTotalEuro = document.getElementById("cart-total-euro");

    const euroTotal = await getCartPrice();

    cartTotalEuro.textContent = `${euroTotal ? euroTotal : 0.00.toFixed(2)} €`;


}




export async function checkLoginStatus(){
    const token = localStorage.getItem("token");
    console.log(token);

    if(token){
        const response = fetch("http://localhost:8080/business/users/status", {
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
            }
        })
//TODO iskam vmesto da pishe profil da e purvata bukva ot imeto

    }
}

