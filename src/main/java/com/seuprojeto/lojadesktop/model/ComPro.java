package com.seuprojeto.lojadesktop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "compras_produtos")
public class ComPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComPro;

    @ManyToOne
    private Compra compra;

    @ManyToOne
    private Produto produto;

    private Double quantidade;

    public ComPro() {
    }

    public Integer getIdComPro() {
        return idComPro;
    }

    public void setIdComPro(Integer idComPro) {
        this.idComPro = idComPro;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
