package model;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Productos {
	
	    private final SimpleStringProperty nombre;
	    private final SimpleStringProperty codigo;
	    private final SimpleStringProperty categoria;
	    private final SimpleStringProperty descripcion;
	    private final SimpleIntegerProperty stock;
	    private final SimpleStringProperty proveedor;
	    private final SimpleObjectProperty<LocalDate> fechaIngreso;
	    private final SimpleStringProperty marca;
	    private final SimpleIntegerProperty stockMinimo;
	    private final SimpleIntegerProperty id;
	    private final SimpleStringProperty imagen;
		
		private String rutaImagen;

	    //Constructor sin parámetros.
	    public Productos() {
	        this.nombre = new SimpleStringProperty();
	        this.codigo = new SimpleStringProperty();
	        this.categoria = new SimpleStringProperty();
	        this.descripcion = new SimpleStringProperty();
	        this.stock = new SimpleIntegerProperty();
	        this.proveedor = new SimpleStringProperty();
	        this.fechaIngreso = new SimpleObjectProperty<>();
	        this.marca = new SimpleStringProperty();
	        this.stockMinimo = new SimpleIntegerProperty();
	        this.id = new SimpleIntegerProperty();
	        this.imagen = new SimpleStringProperty();
	    }
	    
	    //Constructor con parámetros.
	    public Productos(int id, String codigo, String nombre, String descripcion, String categoria,
                int stock, String proveedor, LocalDate fechaIngreso, String imagen) {
			   this.id = new SimpleIntegerProperty(id);
			   this.codigo = new SimpleStringProperty(codigo);
			   this.nombre = new SimpleStringProperty(nombre);
			   this.descripcion = new SimpleStringProperty(descripcion);
			   this.categoria = new SimpleStringProperty(categoria);
			   this.stock = new SimpleIntegerProperty(stock);
			   this.proveedor = new SimpleStringProperty(proveedor);
			   this.fechaIngreso = new SimpleObjectProperty<>(fechaIngreso);
			   this.imagen = new SimpleStringProperty(imagen);
			
			   this.marca = new SimpleStringProperty("");
			   this.stockMinimo = new SimpleIntegerProperty(0);
			}
	    // Getters y setters

	    public SimpleStringProperty getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre.set(nombre);
	    }

	    public StringProperty nombreProperty() {
	        return nombre;
	    }
	    
	    public SimpleStringProperty getCodigo() {
	    	return codigo;
	    }
	    
	    public void setCodigo(String codigo) {
	    	this.codigo.set(codigo);
	    }
	    
	    public StringProperty codigoProperty() {
	    	return codigo;
	    }

		public SimpleStringProperty getCategoria() {
			return categoria;
		}
		
		public void setCategoria(String categoria) {
			this.categoria.set(categoria);
		}
		
		public StringProperty categoriaProperty() {
			return categoria;
		}
		
		public SimpleStringProperty getDescripcion() {
			return descripcion;
		}
		
		public void setDescripcion(String descripcion) {
			this.descripcion.set(descripcion);
		}
		
		public StringProperty descripcionProperty() {
			return descripcion;
		}

		public SimpleIntegerProperty getStock() {
			return stock;
		}
		
		public void setStock(int stock) {
			this.stock.set(stock);
		}
		
		public SimpleIntegerProperty cantidadProperty() {
			return stock;
		}

		public SimpleStringProperty getProveedor() {
			return proveedor;
		}
		
		public void setProveedor(String proveedor) {
			this.proveedor.set(proveedor);
		}
		
		public StringProperty proveedorProperty() {
			return proveedor;
		}

		public SimpleObjectProperty<LocalDate> getFechaIngreso() {
			return fechaIngreso;
		}
		
		public void setFechaIngreso(LocalDate fechaIngreso) {
			this.fechaIngreso.set(fechaIngreso);
		}

		public SimpleStringProperty getMarca() {
			return marca;
		}
		
		public void setMarca (String marca) {
			this.marca.set(marca);
		}
		
		public StringProperty marcaProperty() {
			return marca;
		}

		public SimpleIntegerProperty getStockMinimo() {
			return stockMinimo;
		}
		
		public void setStockMinimo(int stockMinimo) {
			this.stockMinimo.set(stockMinimo);
		}
		
		public SimpleIntegerProperty stockMinimoProperty() {
			return stockMinimo;
		}

		public int getId() {
			return id.get();
		}

		public void setId(int id) {
			this.id.set(id);			
		}
		
		public SimpleIntegerProperty idProperty() {
			return id;
		}
		
		public String getNombreString() {
		    return nombre.get();
		}

		public String getCodigoString() {
		    return codigo.get();
		}

		public String getCategoriaString() {
		    return categoria.get();
		}

		public String getDescripcionString() {
		    return descripcion.get();
		}

		public int getStockMinimoInt() {
		    return stockMinimo.get();
		}

		public int getstockInt() {
		    return stock.get();
		}

		public String getProveedorString() {
		    return proveedor.get();
		}

		public LocalDate getFechaIngresoValue() {
		    return fechaIngreso.get();
		}
		
		public SimpleStringProperty imagenProperty() {
		    return imagen;
		}

		public String getImagen() {
		    return imagen.get();
		}

		public void setImagen(String imagen) {
		    this.imagen.set(imagen);
		}

		public String getRutaImagen() {
		    return rutaImagen;
		}

		public void setRutaImagen(String rutaImagen) {
		    this.rutaImagen = rutaImagen;
		}
	}

