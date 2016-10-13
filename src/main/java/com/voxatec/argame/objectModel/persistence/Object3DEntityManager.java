package com.voxatec.argame.objectModel.persistence;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.File;
import com.voxatec.argame.objectModel.beans.Object3D;


public class Object3DEntityManager extends EntityManager {
	
	
	public Vector<Object3D> getObjects3D() throws SQLException {
		Vector<Object3D> object3DList = new Vector<Object3D>();

		try {
			this.initConnection();

			String stmt = "select * from object3D";
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			Object3D obj3D = null;

			while (resultSet.next()) {
				// Object3D
				obj3D = new Object3D();
				obj3D.setId(resultSet.getInt("id"));
				obj3D.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				obj3D.setText(HtmlUtils.htmlEscape(resultSet.getString("text")));
				obj3D.setObjFileName(HtmlUtils.htmlEscape(resultSet.getString("obj_file_name")));
				object3DList.add(obj3D);
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return object3DList;
	}
	
	
	public Object3D getObject3DById(Integer object3DId) throws SQLException {
		Object3D theObject3D = null;
		
		try {
			this.initConnection();

			String template = "select * from v_object3D where obj_id=%d";
			String stmt = String.format(template, object3DId);
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theObject3D = new Object3D();
				theObject3D.setId(object3DId);
				theObject3D.setName(HtmlUtils.htmlEscape(resultSet.getString("obj_name")));
				theObject3D.setText(HtmlUtils.htmlEscape(resultSet.getString("obj_text")));
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return theObject3D;
	}
	

	public File getDatFile(Integer cacheGroupId) throws SQLException {
		File datFile = new File();
		
        try {
			this.initConnection();

	        String template = "select target_img_dat_file_name, target_img_dat_file from cache_group where id=%d";
	        String stmt = String.format(template, cacheGroupId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("target_img_dat_file");
				if (blob != null) {
					String strContent = new String(blob.getBytes(1l, (int) blob.length()));
					datFile.setContent(HtmlUtils.htmlEscape(strContent));
					datFile.setMimeType("application/octet-stream");
				}
				datFile.setName(resultSet.getString("target_img_dat_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return datFile;
	}
	

	public File getObjFile(Integer object3DId) throws SQLException {
		File objFile = new File();
		
        try {
			this.initConnection();

	        String template = "select obj_file_name, obj_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("obj_file");
				String strContent = new String(blob.getBytes(1l, (int) blob.length()));
        		objFile.setContent(HtmlUtils.htmlEscape(strContent));
        		objFile.setMimeType("text/plain");
				objFile.setName(resultSet.getString("obj_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return objFile;
	}
	
	public File getMtlFile(Integer object3DId) throws SQLException {
		File mtlFile = new File();
		
        try {
			this.initConnection();

	        String template = "select mtl_file_name, mtl_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("mtl_file");
				String strContent = new String(blob.getBytes(1l, (int) blob.length()));
				mtlFile.setContent(HtmlUtils.htmlEscape(strContent));
				mtlFile.setMimeType("text/plain");
				mtlFile.setName(resultSet.getString("mtl_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return mtlFile;
	}
	
	public File getTexFile(Integer object3DId) throws SQLException {
		File texFile = new File();
		
        try {
			this.initConnection();

	        String template = "select tex_file_name, tex_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("tex_file");
				String strContent = new String(blob.getBytes(1l, (int) blob.length()));
				texFile.setContent(HtmlUtils.htmlEscape(strContent));
				texFile.setMimeType("text/plain");
				texFile.setName(resultSet.getString("tex_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return texFile;
	}

		
}
