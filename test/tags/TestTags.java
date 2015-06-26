/**
 * 
 */
package tags;

import org.junit.Test;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.template.tags.ITag;
import org.marker.mushroom.template.tags.impl.AbsoluteURLTagImpl;

/**
 * @author marker
 * @date 2013-8-24 下午12:39:59
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class TestTags {

	@Test
	public void testAbsoluteURLTagImpl() { 
		ITag xx = new AbsoluteURLTagImpl();
		xx.iniContent("<link href=\"$css/dsdsadsa/dsadsad/main.css\" \n"
				+ " src=\"#\"></script>\n"
				+ "<a href=\"http://dsadsa.png\"></a>"
				+ " \n<a href=\"${decoder(article.url)}\">${c.name} </a>\n"
				+ "href=\"${decoder(channel.url)}\">"
				+ "\n<img src=\"${c.dsd}\"/> "
				+ "<!--{if:${channel.id == current.id}}-->\n"
				+ "<li><a href=\"${encoder(channel.url)}\"> </a></li>\n"
				+ "<li><a href=\"${encoder(channel.url)}\"><div>${channel.name}</div></a></li> ");
		try {
			xx.doTag();

		} catch (SystemException e) {
			e.printStackTrace();
		}
		System.out.println(xx.getContent());

	}
	
 
}
