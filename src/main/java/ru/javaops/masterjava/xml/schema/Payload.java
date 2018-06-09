
package ru.javaops.masterjava.xml.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://javaops.ru}Cities"/>
 *         &lt;element ref="{http://javaops.ru}Projects"/>
 *         &lt;element ref="{http://javaops.ru}Users"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Payload", namespace = "http://javaops.ru")
public class Payload {

    @XmlElement(name = "Cities", namespace = "http://javaops.ru", required = true)
    protected Cities cities;
    @XmlElement(name = "Projects", namespace = "http://javaops.ru", required = true)
    protected Projects projects;
    @XmlElement(name = "Users", namespace = "http://javaops.ru", required = true)
    protected Users users;

    /**
     * Gets the value of the cities property.
     * 
     * @return
     *     possible object is
     *     {@link Cities }
     *     
     */
    public Cities getCities() {
        return cities;
    }

    /**
     * Sets the value of the cities property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cities }
     *     
     */
    public void setCities(Cities value) {
        this.cities = value;
    }

    /**
     * Gets the value of the projects property.
     * 
     * @return
     *     possible object is
     *     {@link Projects }
     *     
     */
    public Projects getProjects() {
        return projects;
    }

    /**
     * Sets the value of the projects property.
     * 
     * @param value
     *     allowed object is
     *     {@link Projects }
     *     
     */
    public void setProjects(Projects value) {
        this.projects = value;
    }

    /**
     * Gets the value of the users property.
     * 
     * @return
     *     possible object is
     *     {@link Users }
     *     
     */
    public Users getUsers() {
        return users;
    }

    /**
     * Sets the value of the users property.
     * 
     * @param value
     *     allowed object is
     *     {@link Users }
     *     
     */
    public void setUsers(Users value) {
        this.users = value;
    }

}
