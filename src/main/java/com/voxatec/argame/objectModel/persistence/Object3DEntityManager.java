package com.voxatec.argame.objectModel.persistence;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.File;
import com.voxatec.argame.objectModel.beans.ImageFile;
import com.voxatec.argame.objectModel.beans.Object3D;
import com.voxatec.argame.objectModel.beans.Texture;


public class Object3DEntityManager extends EntityManager {
	
	
	// ---- Get all objects ------------------------------------------------------------------------------
	public Vector<Object3D> getObjects3D() throws SQLException {
		Vector<Object3D> object3DList = new Vector<Object3D>();

		try {
			this.initConnection();

			String stmt = "select * from v_object3D order by obj_name";
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			Object3D obj3D = null;
			Texture texture = null;
			Vector<Texture> textureList = null;

			while (resultSet.next()) {
				// Object3D
				int obj3DId = resultSet.getInt("obj_id");
				
				if (obj3D == null || obj3DId != obj3D.getId()) {
					obj3D = new Object3D();
					obj3D.setId(obj3DId);
					obj3D.setName(HtmlUtils.htmlEscape(resultSet.getString("obj_name")));
					obj3D.setText(HtmlUtils.htmlEscape(resultSet.getString("obj_text")));
					obj3D.setObjFileName(HtmlUtils.htmlEscape(resultSet.getString("obj_obj_file_name")));
					obj3D.setMtlFileName(HtmlUtils.htmlEscape(resultSet.getString("obj_mtl_file_name")));
					obj3D.setObjScaleFactor(resultSet.getDouble("obj_scale_factor"));
					textureList = new Vector<Texture>();
					obj3D.setTextureList(textureList);
					object3DList.add(obj3D);
				}
				
				// Texture
				int textureId = resultSet.getInt("tex_id");
				
				if (!resultSet.wasNull()) {
					texture = new Texture();
					texture.setId(textureId);
					texture.setName(resultSet.getString("tex_name"));
					texture.setImageType(resultSet.getString("tex_type"));
					textureList.add(texture);
				}
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return object3DList;
	}
	
	
	// ---- Get one object by Id -------------------------------------------------------------------------	
	public Object3D getObject3DById(Integer object3DId) throws SQLException {
		Object3D obj3D = null;
		Texture texture = null;
		Vector<Texture> textureList = null;
		
		try {
			this.initConnection();

			String template = "select * from v_object3D where obj_id=%d";
			String stmt = String.format(template, object3DId);
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Object3D
				int obj3DId = resultSet.getInt("obj_id");
				
				if (obj3D == null || obj3DId != obj3D.getId()) {
					obj3D = new Object3D();
					obj3D.setId(obj3DId);
					obj3D.setName(HtmlUtils.htmlEscape(resultSet.getString("obj_name")));
					obj3D.setText(HtmlUtils.htmlEscape(resultSet.getString("obj_text")));
					obj3D.setObjFileName(HtmlUtils.htmlEscape(resultSet.getString("obj_obj_file_name")));
					obj3D.setMtlFileName(HtmlUtils.htmlEscape(resultSet.getString("obj_mtl_file_name")));
					obj3D.setObjScaleFactor(resultSet.getDouble("obj_scale_factor"));
					textureList = new Vector<Texture>();
					obj3D.setTextureList(textureList);
				}
				
				// Texture
				int textureId = resultSet.getInt("tex_id");
				
				if (!resultSet.wasNull()) {
					texture = new Texture();
					texture.setId(textureId);
					texture.setName(resultSet.getString("tex_name"));
					texture.setImageType(resultSet.getString("tex_type"));
					textureList.add(texture);
				}
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return obj3D;
	}
	

	// ---- Create an object ----------------------------------------------------------------------------
	public Object3D createObject3D(Object3D object3D) throws SQLException {
		
		if (object3D == null)
			return null;
		
		try {
			this.initConnection();

			// Insert object3D into DB
			String template = "insert into object3D (name,text) values (\"%s\",\"%s\")";
			String name = HtmlUtils.htmlUnescape(object3D.getName());
			String text = this.queryfiableString(HtmlUtils.htmlUnescape(object3D.getText()));
			String stmt = String.format(template, name, text);
			
			this.connection.executeUpdateStatement(stmt);
			
			// Retrieve auto inserted ID value
			Integer lastInsertedId = this.getLastAutoInsertedId();
			object3D.setId(lastInsertedId);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
		
		return object3D;
	}
	
	
	// ---- Update an object ---------------------------------------------------------------------------
	public void updateObject3D(Object3D object3D) throws SQLException {
		
		if (object3D == null)
			return;
		
		try {
			this.initConnection();

			String template = "update object3D set name=\"%s\", text=\"%s\", obj_scale_factor=%f where id=%d";
			String name = HtmlUtils.htmlUnescape(object3D.getName());
			String text = this.queryfiableString(HtmlUtils.htmlUnescape(object3D.getText()));
			double scaleFactor = object3D.getObjScaleFactor();
			Integer id  = object3D.getId();
			String stmt = String.format(template, name, text, scaleFactor, id);
			
			this.connection.executeUpdateStatement(stmt);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
	
	// ---- Delete object -------------------------------------------------------------------------
	public void deleteObject3D(Integer object3DId) throws SQLException {
		
		Object3D theObject3D = null;
		
		if (object3DId == EntityManager.UNDEF_ID)
			return;   // nothing to do
		
		try {
			theObject3D = this.getObject3DById(object3DId);
			
			this.initConnection();
			
			if (theObject3D != null) {
				// Delete textures
				String template = "delete from texture where object3D_id=%d";
				String stmt = String.format(template, object3DId);
				this.connection.executeUpdateStatement(stmt);
				
				// Delete object3D
				template = "delete from object3D where id=%d";
				stmt = String.format(template, object3DId);
				this.connection.executeUpdateStatement(stmt);
			}
		
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}

	
	
	// ---- Get OBJ file of object -------------------------------------------------------------------------
	public String getObjFile(Integer object3DId) throws SQLException {
		String objFile = null;
		
        try {
			this.initConnection();

	        String template = "select obj_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("obj_file");
				if (blob != null) {
					objFile = new String(blob.getBytes(1l, (int) blob.length()));
					objFile = HtmlUtils.htmlEscape(objFile);
				}
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return objFile;
	}
	
	
	// ---- Update OBJ file of object ----------------------------------------------------------------------
	public void updateObjFile(File objFile, Integer object3DId) throws SQLException {
		
		try {
			this.initConnection();
			
			PreparedStatement stmt = this.connection.newPreparedStatement("update object3D set obj_file=?, obj_file_name=? where id=?");
			
			// Unescape file content
			String fileContent = HtmlUtils.htmlUnescape(objFile.getContent());
			
			// Bind statement parameters & execute
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, fileContent.getBytes());
			stmt.setBlob(1, blob);
	        stmt.setString(2, objFile.getName());
	        stmt.setInt(3, object3DId);
	        stmt.executeUpdate();
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	}
	
	
	// ---- Get MTL file of object -------------------------------------------------------------------------
	public String getMtlFile(Integer object3DId) throws SQLException {
		String mtlFile = null;
		
        try {
			this.initConnection();

	        String template = "select mtl_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("mtl_file");
				if (blob != null) {
					mtlFile = new String(blob.getBytes(1l, (int) blob.length()));
					mtlFile = HtmlUtils.htmlEscape(mtlFile);
				}
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
		
		return mtlFile;
	}
	
	
	// ---- Update MTL file of object ----------------------------------------------------------------------
	public void updateMtlFile(File mtlFile, Integer object3DId) throws SQLException {
		
		try {
			this.initConnection();
			
			PreparedStatement stmt = this.connection.newPreparedStatement("update object3D set mtl_file=?, mtl_file_name=? where id=?");
			
			// Unescape file content
			String fileContent = HtmlUtils.htmlUnescape(mtlFile.getContent());
			
			// Bind statement parameters & execute
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, fileContent.getBytes());
			stmt.setBlob(1, blob);
	        stmt.setString(2, mtlFile.getName());
	        stmt.setInt(3, object3DId);
	        stmt.executeUpdate();
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	}
	
	
	// ---- Create TEX image --------------------------------------------------------------------------
	public void createTexFile(File texFile, Integer object3DId) throws Exception {
		
		if (object3DId == EntityManager.UNDEF_ID)
			return;
		
		try {
			this.createImageFile(
					texFile, 
	                "texture", 
	                "image", "image_type", "name",
	                "object3D_id",
	                object3DId);
			
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;
		}
	}
	
	
	// ---- Get Texture image file of object3D ---------------------------------------------------------------
	public ImageFile getTextureFile(Integer object3DId, String texFileName) throws Exception {
		ImageFile imgFile = null;
		
		try {
			imgFile = this.getImageFileByBeanIdAndName("texture", 
					                                   "image", "image_type", "name", "object3D_id", 
				                                       object3DId, texFileName);
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		}
		
		return imgFile;
	}
	
	// ---- Update TEX image file of object ----------------------------------------------------------------
	/*
	public void updateTextureFile(File texFile, Integer object3DId) throws SQLException {
		
		try {
			this.initConnection();
			
			PreparedStatement stmt = this.connection.newPreparedStatement("update object3D set tex_file=?, tex_file_name=? where id=?");
			
			// Decode tex image file from Base64
			byte[] image = null;
			if (texFile != null && texFile.getContent() != null) {
				byte[] bytes = texFile.getContent().getBytes();
				image = Base64.getDecoder().decode(bytes);
			}
			// System.out.println(String.format("decoded size: %d", decoded.length));
			
			// Bind statement parameters & execute
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, image);
			stmt.setBlob(1, blob);
	        stmt.setString(2, texFile.getName());
	        stmt.setInt(3, object3DId);
	        stmt.executeUpdate();
			
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	}
	*/
	
	public void updateTextureFile(File texFile, Integer object3DId) throws SQLException, UnsupportedEncodingException, URISyntaxException, IOException {
		
		this.updateImageFileByBeanIdAndName(
				texFile, 
                "texture", 
                "image", "image_type", "name",
                "object3D_id",
                object3DId);
	}
	
	// ---- Delete TEX image file of object ----------------------------------------------------------------
	public void deleteTextureFile(String texFileName, Integer object3DId) throws SQLException {
		
		if (object3DId == EntityManager.UNDEF_ID)
			return;   // nothing to do
		
		try {
			this.initConnection();
			
			// Delete textures
			String template = "delete from texture where object3D_id=%d and (name=\"" + texFileName + "\" or name like \"" + texFileName + ".%s\")";
			String stmt = String.format(template, object3DId, "%");
			this.connection.executeSelectStatement(stmt);
		
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}

		
}
