package cc.ryanc.halo.utils;

import cc.ryanc.halo.model.dto.HaloConst;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : RYAN0UP
 * @date : 2018/11/14
 */
public class MarkdownUtils {

    /**
     * Front-matter 插件
     */
    private static final Set<Extension> EXTENSIONS_YAML = Collections.singleton(YamlFrontMatterExtension.create());

    /**
     * Table 插件
     */
    private static final Set<Extension> EXTENSIONS_TABLE = Collections.singleton(TablesExtension.create());

    /**
     * 解析 Markdown 文档
     */
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS_YAML).extensions(EXTENSIONS_TABLE).build();

    /**
     * 渲染 HTML 文档
     */
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS_YAML).extensions(EXTENSIONS_TABLE).build();

    /**
     * 渲染 Markdown
     *
     * @see https://github.com/otale/tale/blob/master/src/main/java/com/tale/utils/TaleUtils.java
     * @param content content
     * @return String
     */
    public static String renderMarkdown(String content) {
        final Node document = PARSER.parse(content);
        String renderContent = RENDERER.render(document);
        // render netease music short url
        if (content.contains(HaloConst.NETEASE_MUSIC_PREFIX)) {
            renderContent = content.replaceAll(HaloConst.NETEASE_MUSIC_REG_PATTERN, HaloConst.NETEASE_MUSIC_IFRAME);
        }
        return renderContent;
    }

    /**
     * 获取元数据
     *
     * @param content content
     * @return Map
     */
    public static Map<String, List<String>> getFrontMatter(String content) {
        final YamlFrontMatterVisitor visitor = new YamlFrontMatterVisitor();
        final Node document = PARSER.parse(content);
        document.accept(visitor);
        return visitor.getData();
    }
}
