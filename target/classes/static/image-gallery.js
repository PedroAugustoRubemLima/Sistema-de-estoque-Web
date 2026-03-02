async function carregarImagens() {
    const response = await fetch("/api/imagens");
    const imagens = await response.json();

    const galeria = document.getElementById("galeria");
    galeria.innerHTML = "";

    imagens.forEach(src => {
        const img = document.createElement("img");
        img.src = src;
        galeria.appendChild(img);
    });
}

carregarImagens();
