import numpy
import cv2
from fer import FER

detector = FER()

def main(data):
    decoded = base64.b64decode(data)
    np = numpy.fromstring(decoded,numpy.uint8)
    img = cv2.imdecode(np,cv2.IMREAD_UNCHANGED)
    return detector.top_emotion(img)