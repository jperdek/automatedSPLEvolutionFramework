import time

import requests
from bs4 import BeautifulSoup
import re as regexp
from screenshoting.screenshooter import PlaywrightScreenshooter


class SVGImplementation:
    ON_READY_JQUERY = 1
    TRANSPILE_TS = 2

class SVGTransform:

    @staticmethod
    def __get_svg_creation_script() -> str:
        return """
            function SvgCreator(id, width, height, margin, linkId){
                this.margin = margin;
                this.id = id;
                this.createLine;
                this.svgElement;
                
                this.appendSVGLink = function(tagName, linkId){
                    var linkSVG = document.createElement('A');
                    linkSVG.appendChild(this.svgElement);
                    linkSVG.id= linkId;
                    document.getElementsByTagName(tagName)[0].appendChild(linkSVG);
                }
                
                this.saveSVG = function(svgId, linkId) {
                    this.createDownloadableLink(svgId, linkId);
                }
                
                this.createDownloadableLink = function(id, linkId){
                    var svgData = document.getElementById(id).outerHTML;
                    var svgBlob = new Blob([svgData], {type:"image/svg+xml;charset=utf-8"});
                    var svgUrl = URL.createObjectURL(svgBlob);
                    var link = document.getElementById(linkId)
                    link.href = svgUrl;
                    link.download = "fractal.svg";
                }
                
                this.createSVG = function(id, width, height){
                    var svgElement = document.createElementNS("http://www.w3.org/2000/svg", "svg");
                    svgElement.id = id;
                    svgElement.setAttribute("width", String(width));
                    svgElement.setAttribute("height", String(height));
                    return svgElement;
                }
                
                this.svgElement = this.createSVG(id, width + margin, height + margin);
                this.appendSVGLink("body", linkId);
                
                this.createLine = function(x1, y1, x2, y2, strokeWidth, strokeColorString, lineCapString){
                    var newLine =  document.createElementNS("http://www.w3.org/2000/svg", "line");
                    newLine.setAttribute("x1", Math.round(x1));
                    newLine.setAttribute("y1", Math.round(y1));
                    newLine.setAttribute("x2", Math.round(x2));
                    newLine.setAttribute("y2", Math.round(y2));
                    
                    if(strokeWidth == null){
                        newLine.setAttribute("stroke-width", 1);
                    } else {
                        newLine.setAttribute("stroke-width", strokeWidth);
                    }
                    
                    if(strokeColorString == null){
                        newLine.setAttribute("stroke", "black");
                    } else {
                        newLine.setAttribute("stroke", strokeColorString);
                    }
                    
                    if(lineCapString != null){
                        newLine.setAttribute("stroke-linecap", lineCapString);
                    }
                    
                    this.svgElement.appendChild(newLine);
                    
                    return newLine;
                }	
            }
            """

    # only x1, y1, x2, y2 and thickness are propagated to original svg!!!
    @staticmethod
    def __get_svg_transformation_script(generated_svg_id: str = "svgId", draw_line_in_op: str = "drawLine",
                                        replacement_function: str = "function(context, x1, y1, x2, y2, thickness)") -> str:
        return """
            var svgCreator = new SvgCreator(\"""" + generated_svg_id + """\", 10000, 10000, 25, "linkId");
            svgCreator.minX = 10000000;
            svgCreator.maxX = -1;
            svgCreator.minY = 10000000;
            svgCreator.maxY = -1;
            
            var createLineContext = """ + replacement_function + """ {
                if (x1 < svgCreator.minX) { svgCreator.minX = x1; }
                if (x1 > svgCreator.maxX) { svgCreator.maxX = x1; }
                if (y1 < svgCreator.minY) { svgCreator.minY = y1; }
                if (y1 > svgCreator.maxY) { svgCreator.maxY = y1; }
                    if (x2 < svgCreator.minX) { svgCreator.minX = x2; }
                if (x2 > svgCreator.maxX) { svgCreator.maxX = x2; }
                if (y2 < svgCreator.minY) { svgCreator.minY = y2; }
                if (y2 > svgCreator.maxY) { svgCreator.maxY = y2; }
                svgCreator.createLine(x1, y1, x2, y2, thickness, null, null);
            }
            
            """ + draw_line_in_op + """ = createLineContext;
        """

    @staticmethod
    def __get_finalizing_svg_script(generated_svg_id: str = "svgId") -> str:
        return """
            var svgElFractal = document.getElementById(\"""" + generated_svg_id + """\");
            svgElFractal.setAttribute("width", String(svgCreator.maxX));
            svgElFractal.setAttribute("height", String(svgCreator.maxY));
        """

    @staticmethod
    def transform_into_svg(web_page_location: str, generated_svg_id: str, draw_line_in_op: str,
                           replacement_function: str, time_to_wait: int = 10, implementation_type: SVGImplementation = SVGImplementation.TRANSPILE_TS) -> str:
        screenshooter = PlaywrightScreenshooter("chromium")
        page = screenshooter.new_page()
        if "http" not in web_page_location:
            with open(web_page_location, "r", encoding="utf-8") as file:
                html_to_parse = file.read()
        else:
            html_to_parse = requests.get(web_page_location).content
        page_soup = BeautifulSoup(html_to_parse, "lxml")

        script_code1 = SVGTransform.__get_svg_creation_script()
        script_code2 = SVGTransform.__get_svg_transformation_script(generated_svg_id, draw_line_in_op, replacement_function)
        if implementation_type == SVGImplementation.TRANSPILE_TS:
            for script in page_soup.select("script"):
                script_text = regexp.sub(r"//[^\n]+", "", script.text, regexp.MULTILINE)
                if regexp.search(r"((var|let|const)\s+canvas\s*=)", script_text, regexp.MULTILINE):

                    to_split = regexp.search(r"((?:var|let|const)\s+canvas\s*=[^;]+;)", script_text, regexp.MULTILINE).group(1)
                    parts = script_text.split(to_split)

                    script.string = parts[0] + script_code1 + script_code2 + to_split + to_split.join(parts[1:])
                    break
        else:
            page_body = page_soup.select_one("body")
            new_script = page_soup.new_tag("script")
            new_script.string = script_code1

            new_script2 = page_soup.new_tag("script")
            new_script2.string = script_code2
            page_body.append(new_script)
            page_body.append(new_script2)
        file_name = web_page_location[web_page_location.rfind("/") + 1:]
        new_file_name = web_page_location.replace(file_name, "") + "__COPY__" + file_name
        with open(new_file_name, "w", encoding="utf-8") as file:
            file.write(str(page_soup))
        page.goto(new_file_name, wait_until="networkidle")

        time.sleep(time_to_wait)

        page.evaluate(SVGTransform.__get_finalizing_svg_script(generated_svg_id))
        svg_element = page.locator("#" + generated_svg_id).evaluate("el => el.outerHTML")
        return svg_element
